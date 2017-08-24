/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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

package shade.sun.tools.attach.spi;

import shade.sun.tools.attach.AttachNotSupportedException;
import shade.sun.tools.attach.AttachPermission;
import shade.sun.tools.attach.VirtualMachine;
import shade.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AttachProvider {

	private static final Object lock = new Object();
	private static List<AttachProvider> providers = new ArrayList<AttachProvider>();

	public static void setAttachProvider(AttachProvider provider) {
		synchronized (lock) {
			providers.clear();
			providers.add(provider);
		}
	}

	protected AttachProvider() {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkPermission(new AttachPermission("createAttachProvider"));
	}

	public abstract String name();

	public abstract String type();

	public abstract VirtualMachine attachVirtualMachine(String id) throws AttachNotSupportedException, IOException;

	public VirtualMachine attachVirtualMachine(VirtualMachineDescriptor vmd) throws AttachNotSupportedException, IOException {
		if (vmd.provider() != this) {
			throw new AttachNotSupportedException("provider mismatch");
		}
		return attachVirtualMachine(vmd.id());
	}

	public abstract List<VirtualMachineDescriptor> listVirtualMachines();

	public static List<AttachProvider> providers() {
		synchronized (lock) {
			return Collections.unmodifiableList(providers);
		}
	}

}
