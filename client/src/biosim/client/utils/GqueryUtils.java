package biosim.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;

public class GqueryUtils {
	
	public static void center(Element e) {
		Element parent = e.getParentElement();
		int parentHeight = parent.getClientHeight();
		if(parentHeight == 0) {
			Style style = parent.getStyle();
			String height = style.getHeight();
			height = height.replaceAll("[a-zA-Z]", "");
			parentHeight = Integer.valueOf(height);
		}
		int myHeight = e.getOffsetHeight();
		int heightDiff = parentHeight - myHeight;
		if(heightDiff > 0) {
			e.getStyle().setMarginTop(heightDiff/2, Unit.PX);
		}
	}
	
	public static void fill(Element e) {
		int parentHeight = e.getParentElement().getClientHeight();
		Style parentStyle = e.getParentElement().getStyle();
		int paddingTop = 0;
		try {
			if(parentStyle.getPaddingTop() != null && parentStyle.getPaddingTop().length() > 0) {
				paddingTop = Integer.parseInt(parentStyle.getPaddingTop());
			}
		} catch (Exception ex) {}
		int paddingBottom = 0;
		try {
			if(parentStyle.getPaddingBottom() != null && parentStyle.getPaddingBottom().length() > 0) {
				paddingBottom = Integer.parseInt(parentStyle.getPaddingBottom());
			}
		} catch (Exception ex) {}
		parentHeight -= paddingTop;
		parentHeight -= paddingBottom;
		e.getStyle().setHeight(parentHeight, Unit.PX);
	}
	
	public static void setVisibility(Element e, Visibility visibility) {
		e.getStyle().setVisibility(visibility);
	}

}
