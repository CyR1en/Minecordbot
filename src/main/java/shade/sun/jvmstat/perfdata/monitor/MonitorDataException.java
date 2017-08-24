/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package shade.sun.jvmstat.perfdata.monitor;

import shade.sun.jvmstat.monitor.MonitorException;

/**
 * Exception indicating that improperly formatted data was encountered while parsing a HotSpot PerfData buffer.
 *
 * @author Brian Doherty
 * @since 1.5
 */
@SuppressWarnings("serial")
public class MonitorDataException extends MonitorException {

	/**
	 * Constructs a <code>MonitorDataException</code> with <code>
	 * null</code> as its error detail message.
	 */
	public MonitorDataException() {
		super();
	}

	/**
	 * Constructs an <code>MonitorDataException</code> with the specified detail message. The error message string <code>s</code> can later be retrieved by the <code>{@link Throwable#getMessage}</code> method of class <code>java.lang.Throwable</code>.
	 *
	 * @param s
	 *            the detail message.
	 */
	public MonitorDataException(String s) {
		super(s);
	}
}
