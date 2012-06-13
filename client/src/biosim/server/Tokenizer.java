package biosim.server;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private boolean includeEmptyTokens_;

    private boolean reachedEndOfText_ = false;
    
	private String delimiters_;
	private String text_;
	private int index_;
	private int currentTokenStart_;
	private int currentTokenEnd_;
	private String currentToken_;


	public Tokenizer( String text ) {
		this( text, " \t\n\r\f" );
	}

	public Tokenizer( String text, String delimiters ) {
		this( text, delimiters, false );
	}

	public Tokenizer( String text, String delimiters, boolean includeEmptyTokens ) {
		index_ = 0;
		text_ = text;
		delimiters_ = delimiters;
		includeEmptyTokens_ = includeEmptyTokens;
	}

	public boolean isSeparator( char ch ) {
		return delimiters_.indexOf( ch ) >= 0;
	}

	public String nextToken() {
	    if ( next() ) {
	        return currentToken();
	    } else {
	        return null;
	    }
	}
	
	public boolean next() {
		
	    currentToken_ = null;
		if ( reachedEndOfText_ ) {
			return false;
		}
		
		if ( index_ == text_.length() ) {
	        reachedEndOfText_ = true;
		    if ( includeEmptyTokens_ ) {
		        currentToken_ = "";
		        currentTokenStart_ = currentTokenEnd_ = text_.length();
		        return true;
		    } else {
		        return false;
		    }
		}
		
		boolean validToken = false;
		currentTokenStart_ = index_;
		while ( true ) {
		    char ch = text_.charAt( index_ );
			if ( isSeparator( ch ) ) {
				if ( includeEmptyTokens_ || validToken ) {
					break;
				}
				currentTokenStart_ = index_ + 1;
			} else {
				if ( includeEmptyTokens_ || !Character.isWhitespace( ch )  ) {
				    validToken = true;				    
				}
			}
			
			index_++;
			if ( index_ != text_.length() ) {
			    continue;
			}
			
			if ( includeEmptyTokens_ == false && !validToken ) {
				return false;
			}
			
			reachedEndOfText_ = true;
			break;
		} 
		
		currentTokenEnd_ = index_ - 1;
		currentToken_ = text_.substring( currentTokenStart_, currentTokenEnd_ + 1 );
		
		if ( includeEmptyTokens_ ) {
			index_++;
		}
		
		return true;
	}

	public String currentToken() {
		return currentToken_;
	}

	public String[] getAllTokens() {
		return getAllTokens( false );
	}

	public String[] getAllTokens( boolean trim ) {
		
		List<String> list = new ArrayList<String>();
		
		if ( currentToken() != null ) {
			String token = currentToken();
			list.add( token );
		}
		
		while( next() ) {
			String token = currentToken();
			if ( trim ) {
			    token = token.trim();
			}
			list.add( token );
		}
		
		return (String[]) list.toArray( new String[ list.size() ] );
		
	}

	public int currentTokenStart() {
		return currentTokenStart_;
	}

	public int currentTokenEnd() {
		return currentTokenEnd_;
	}

}
