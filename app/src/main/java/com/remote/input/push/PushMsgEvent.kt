package com.remote.input.push

data class PushMsgEvent(val action: String, val msg: String)
data class PushConnectedEvent(var action: String?=null)
