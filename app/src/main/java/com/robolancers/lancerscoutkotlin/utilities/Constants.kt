package com.robolancers.lancerscoutkotlin.utilities

class Constants {
    companion object {
        // Message types sent from the BluetoothChatService Handler
        var MESSAGE_STATE_CHANGE = 1
        var MESSAGE_READ = 2
        var MESSAGE_WRITE = 3
        var MESSAGE_DEVICE_NAME = 4
        var MESSAGE_TOAST = 5

        // Key names received from the BluetoothChatService Handler
        var DEVICE_NAME = "device_name"
        var TOAST = "toast"
    }
}