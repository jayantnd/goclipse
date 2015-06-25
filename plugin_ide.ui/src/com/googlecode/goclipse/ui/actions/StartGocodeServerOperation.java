/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;

import com.googlecode.goclipse.core.tools.GocodeServerManager;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.actions.AbstractUIOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class StartGocodeServerOperation extends AbstractUIOperation {
	
	protected final GocodeServerManager gocodeServerManager;
	protected IPath gocodePath;
	
	public StartGocodeServerOperation(GocodeServerManager gocodeServerManager) {
		super("Starting gocode server");
		this.gocodeServerManager = gocodeServerManager;
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		if (ToolchainPreferences.AUTO_START_DAEMON.get() == false) {
			throw new CoreException(Status.OK_STATUS); // stop operation
		}
		
		gocodePath = GocodeServerManager.getGocodePath();
		boolean needsStart = gocodeServerManager.prepareServerStart(gocodePath);
		if(needsStart == false) {
			throw new CoreException(Status.OK_STATUS); // stop operation
		}
	}
	
	@Override
	protected void performLongRunningComputation(IProgressMonitor monitor) throws CoreException, CommonException,
			OperationCancellation {
		gocodeServerManager.doStartServer(gocodePath, monitor);
	}
	
	@Override
	protected void handleComputationResult() throws CoreException {
	}
	
}