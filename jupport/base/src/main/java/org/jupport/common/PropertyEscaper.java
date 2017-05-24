package org.jupport.common;

import org.apache.commons.lang3.text.translate.UnicodeEscaper;

public class PropertyEscaper extends UnicodeEscaper {

	  /**
	   * Constructor using {@link UnicodeEscaper#UnicodeEscaper(int, int, boolean)}
	   */
	  public PropertyEscaper() {
	    super(0x007F, Integer.MAX_VALUE, true);
	  }

}
