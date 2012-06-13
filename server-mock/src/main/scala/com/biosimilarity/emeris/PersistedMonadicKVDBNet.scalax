package com.biosimilarity.emeris

import com.biosimilarity.lift.model.store.PersistedMonadicKVDBNodeScope
import com.biosimilarity.lift.lib.UUIDOps
import com.biosimilarity.lift.lib.SpecialKURIDefaults
import com.biosimilarity.lift.lib.moniker.identityConversions
import com.biosimilarity.lift.model.store.MonadicTermTypes
import com.biosimilarity.lift.model.store.DistributedAskTypes
import com.biosimilarity.lift.model.store.CnxnCtxtLeaf
import com.biosimilarity.lift.model.msg.JustifiedRequest
import com.biosimilarity.lift.model.msg.JustifiedResponse
import java.net.URI
import com.biosimilarity.lift.lib.moniker.MURI
import biz.source_code.base64Coder.Base64Coder
import com.biosimilarity.lift.lib.AMQPMonikerOps
import com.biosimilarity.lift.model.store.CCnxn
import com.biosimilarity.lift.model.store.CnxnCtxtLabel
import com.biosimilarity.lift.model.store.Cnxn
import com.biosimilarity.lift.model.store.ExcludedMiddleTypes
import com.biosimilarity.lift.model.store.Factual
import java.io.ObjectInputStream
import com.biosimilarity.lift.model.store.CnxnCtxtBranch
import scala.xml.Elem
import com.biosimilarity.lift.model.store.Blobify
import java.io.ObjectOutputStream
import scala.collection.mutable.LinkedHashMap
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import scala.util.continuations._
import scala.concurrent.cpsops
import scala.annotation.unchecked.uncheckedStable
import scala.annotation.unchecked.uncheckedVariance

object PersistedMonadicKVDBNet
  extends PersistedMonadicKVDBNodeScope[String, String, String, String]
  with UUIDOps {
  import SpecialKURIDefaults._
  import identityConversions._
  import cpsops._

  type MTTypes = MonadicTermTypes[String, String, String, String]
  object TheMTT extends MTTypes with Serializable
  override def protoTermTypes: MTTypes = TheMTT

  type DATypes = DistributedAskTypes
  object TheDAT extends DATypes with Serializable
  override def protoAskTypes: DATypes = TheDAT

  override type MsgTypes = DTSMSHRsrc
  override type RsrcMsgTypes = DTSMSHRsrc

  val protoDreqUUID = getUUID()
  val protoDrspUUID = getUUID()

  lazy val aLabel = new CnxnCtxtLeaf[String, String, String](Left("a"))

  object MonadicDRsrcMsgs extends RsrcMsgTypes with Serializable {

    override def protoDreq: DReq = MDGetRequest(aLabel)
    override def protoDrsp: DRsp = MDGetResponse(aLabel, "")
    override def protoJtsreq: JTSReq =
      JustifiedRequest(
        protoDreqUUID,
        new URI("agent", protoDreqUUID.toString, "/invitation", ""),
        new URI("agent", protoDreqUUID.toString, "/invitation", ""),
        getUUID(),
        protoDreq,
        None)
    override def protoJtsrsp: JTSRsp =
      JustifiedResponse(
        protoDreqUUID,
        new URI("agent", protoDrspUUID.toString, "/invitation", ""),
        new URI("agent", protoDrspUUID.toString, "/invitation", ""),
        getUUID(),
        protoDrsp,
        None)
    override def protoJtsreqorrsp: JTSReqOrRsp =
      Left(protoJtsreq)
  }

  override def protoMsgs: MsgTypes = MonadicDRsrcMsgs
  override def protoRsrcMsgs: RsrcMsgTypes = MonadicDRsrcMsgs

  object Being extends PersistenceScope with Serializable {
    override type EMTypes = ExcludedMiddleTypes[mTT.GetRequest, mTT.GetRequest, mTT.Resource]
    object theEMTypes extends ExcludedMiddleTypes[mTT.GetRequest, mTT.GetRequest, mTT.Resource]
      with Serializable {
      case class PrologSubstitution(soln: LinkedHashMap[String, CnxnCtxtLabel[String, String, String]])
        extends Function1[mTT.Resource, Option[mTT.Resource]] {
        override def apply(rsrc: mTT.Resource) = {
          Some(mTT.RBoundHM(Some(rsrc), Some(soln)))
        }
      }
      override type Substitution = PrologSubstitution
    }

    override def protoEMTypes: EMTypes =
      theEMTypes

    object PersistedKVDBNodeFactory extends PersistedKVDBNodeFactoryT {
      def mkCache(here: URI): PersistedMonadicKVDB = {
        new PersistedMonadicKVDB(MURI(here)) with Blobify with AMQPMonikerOps {
          class StringXMLDBManifest(
            override val storeUnitStr: String,
            @transient override val labelToNS: Option[String => String],
            @transient override val textToVar: Option[String => String],
            @transient override val textToTag: Option[String => String])
            extends XMLDBManifest(database) {
            override def valueStorageType: String = {
              throw new Exception("valueStorageType not overriden in instantiation")
            }
            override def continuationStorageType: String = {
              throw new Exception("continuationStorageType not overriden in instantiation")
            }

            override def storeUnitStr[Src, Label, Trgt](cnxn: Cnxn[Src, Label, Trgt]): String = {
              cnxn match {
                case CCnxn(s, l, t) => s.toString + l.toString + t.toString
              }
            }

            def kvNameSpace: String = "record"
            def kvKNameSpace: String = "kRecord"

            def compareNameSpace(ns1: String, ns2: String): Boolean = {
              ns1.equals(ns2)
            }

            override def asStoreValue(
              rsrc: mTT.Resource): CnxnCtxtLeaf[String, String, String] with Factual = {
              tweet(
                "In asStoreValue on " + this + " for resource: " + rsrc)
              val storageDispatch =
                rsrc match {
                  case k: mTT.Continuation => {
                    tweet(
                      "Resource " + rsrc + " is a continuation")
                    continuationStorageType
                  }
                  case _ => {
                    tweet(
                      "Resource " + rsrc + " is a value")
                    valueStorageType
                  }
                };

              tweet(
                "storageDispatch: " + storageDispatch)

              val blob =
                storageDispatch match {
                  case "Base64" => {
                    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
                    val oos: ObjectOutputStream = new ObjectOutputStream(baos)
                    oos.writeObject(rsrc.asInstanceOf[Serializable])
                    oos.close()
                    new String(Base64Coder.encode(baos.toByteArray()))
                  }
                  case "CnxnCtxtLabel" => {
                    tweet(
                      "warning: CnxnCtxtLabel method is using XStream")
                    toXQSafeJSONBlob(rsrc)
                  }
                  case "XStream" => {
                    tweet(
                      "using XStream method")

                    toXQSafeJSONBlob(rsrc)
                  }
                  case _ => {
                    throw new Exception("unexpected value storage type")
                  }
                }
              new CnxnCtxtLeaf[String, String, String](
                Left[String, String](blob))
            }

            def asCacheValue(
              ccl: CnxnCtxtLabel[String, String, String]): String = {
              tweet(
                "converting to cache value")
              ccl match {
                case CnxnCtxtBranch(
                  "string",
                  CnxnCtxtLeaf(Left(rv)) :: Nil
                  ) => {
                  val unBlob =
                    fromXQSafeJSONBlob(rv)

                  unBlob match {
                    case rsrc: mTT.Resource => {
                      getGV(rsrc).getOrElse("a random string")
                    }
                  }
                }
                case _ => {
                  //asPatternString( ccl )
                  throw new Exception("unexpected value form: " + ccl)
                }
              }
            }

            override def asResource(
              key: mTT.GetRequest, // must have the pattern to determine bindings
              value: Elem): emT.PlaceInstance = {
              val ttt = (x: String) => x

              val ptn = asPatternString(key)
              //println( "ptn : " + ptn )     

              val oRsrc: Option[emT.PlaceInstance] =
                for (
                  ltns <- labelToNS;
                  ttv <- textToVar;
                  ccl <- xmlIfier.fromXML(ltns, ttv, ttt)(value)
                ) yield {
                  ccl match {
                    case CnxnCtxtBranch(ns, k :: v :: Nil) => {
                      val oGvOrK =
                        (if (compareNameSpace(ns, kvNameSpace)) {
                          // BUGBUG -- LGM need to return the Solution
                          // Currently the PersistenceManifest has no access to the
                          // unification machinery          

                          for (vCCL <- asCacheValue(ltns, ttv, value)) yield {
                            Left[mTT.Resource, mTT.Resource](mTT.Ground(vCCL))
                          }
                        } else {
                          if (compareNameSpace(ns, kvKNameSpace)) {
                            Some(Right[mTT.Resource, mTT.Resource](asCacheK(v)))
                          } else {
                            throw new Exception("unexpected namespace : (" + ns + ")")
                          }
                        });

                      val cclKey =
                        xmlIfier.fromXML(ltns, ttv, ttt)(
                          xmlIfier.asXML(key)) match {
                            case Some(cclX) => cclX
                            case _ => throw new Exception("xml roundtrip failed " + key)
                          }

                      val Some(soln) = matchMap(cclKey, k)

                      emT.PlaceInstance(
                        k,
                        oGvOrK match {
                          case Some(Left(gv)) => {
                            Left[mTT.Resource, List[Option[mTT.Resource] => Unit @suspendable]](gv)
                          }
                          case Some(Right(mTT.Continuation(ks))) => {
                            Right[mTT.Resource, List[Option[mTT.Resource] => Unit @suspendable]](ks)
                          }
                          case _ => {
                            throw new Exception("excluded middle contract broken: " + oGvOrK)
                          }
                        },
                        // BUGBUG -- lgm : why can't the compiler determine
                        // that this cast is not necessary?
                        theEMTypes.PrologSubstitution(soln).asInstanceOf[emT.Substitution])
                    }
                    case _ => {
                      throw new Exception("unexpected record format : " + value)
                    }
                  }
                }

              // BUGBUG -- lgm : this is a job for flatMap
              oRsrc match {
                case Some(pI) => {
                  pI
                }
                case _ => {
                  throw new Exception("violated excluded middle : " + oRsrc)
                }
              }
            }

          }
          override def asCacheK(
            ccl: CnxnCtxtLabel[String, String, String]): Option[mTT.Continuation] = {
            tweet(
              "converting to cache continuation stack" + ccl)
            ccl match {
              case CnxnCtxtBranch(
                "string",
                CnxnCtxtLeaf(Left(rv)) :: Nil
                ) => {
                val unBlob =
                  continuationStorageType match {
                    case "CnxnCtxtLabel" => {
                      // tweet(
                      //            "warning: CnxnCtxtLabel method is using XStream"
                      //          )
                      fromXQSafeJSONBlob(rv)
                    }
                    case "XStream" => {
                      fromXQSafeJSONBlob(rv)
                    }
                    case "Base64" => {
                      val data: Array[Byte] = Base64Coder.decode(rv)
                      val ois: ObjectInputStream =
                        new ObjectInputStream(new ByteArrayInputStream(data))
                      val o: java.lang.Object = ois.readObject();
                      ois.close()
                      o
                    }
                  }

                unBlob match {
                  case k: mTT.Resource => {
                    Some(k.asInstanceOf[mTT.Continuation])
                  }
                  case _ => {
                    throw new Exception(
                      (
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                        + "ill-formatted continuation stack blob : " + rv
                        + "\n"
                        + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                        + "\n"
                        + "unBlob : " + unBlob
                        + "\n"
                        + "unBlob type : " + unBlob
                        + "\n"
                        + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"))
                  }
                }
              }
              case _ => {
                throw new Exception("ill-formatted continuation stack leaf: " + ccl)
              }
            }
          }

          override def asCacheK(
            ltns: String => String,
            ttv: String => String,
            value: Elem): Option[mTT.Continuation] = {
            throw new Exception("shouldn't be calling this version of asCacheK")
          }
          override def persistenceManifest: Option[PersistenceManifest] = {
            val sid = Some((s: String) => s)
            val kvdb = this;
            Some(
              new StringXMLDBManifest(dfStoreUnitStr, sid, sid, sid) {
                override def valueStorageType: String = {
                  kvdb.valueStorageType
                }
                override def continuationStorageType: String = {
                  kvdb.continuationStorageType
                }
              })
          }
          def dfStoreUnitStr: String = mnkrExchange(name)
        }
      }
      def ptToPt(here: URI, there: URI): PersistedMonadicKVDBNode = {
        val node =
          PersistedMonadicKVDBNode(
            mkCache(MURI(here)),
            List(MURI(there)))
        spawn { node.dispatchDMsgs() }
        node
      }
      def loopBack(here: URI): PersistedMonadicKVDBNode = {
        val exchange = uriExchange(here)
        val hereNow =
          new URI(
            here.getScheme,
            here.getUserInfo,
            here.getHost,
            here.getPort,
            "/" + exchange + "Local",
            here.getQuery,
            here.getFragment)
        val thereNow =
          new URI(
            here.getScheme,
            here.getUserInfo,
            here.getHost,
            here.getPort,
            "/" + exchange + "Remote",
            here.getQuery,
            here.getFragment)

        val node =
          PersistedMonadicKVDBNode(
            mkCache(MURI(hereNow)),
            List(MURI(thereNow)))
        spawn { node.dispatchDMsgs() }
        node
      }
    }
  }

}

