package com.alphaone.synapseshade.ssstart.update

interface UpdateManager { fun check() }
class NoOpUpdateManager : UpdateManager { override fun check() {} }
// v1.1: implementar DriveUpdateManager aqui
