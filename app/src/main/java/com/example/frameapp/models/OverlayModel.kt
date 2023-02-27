package com.example.frameapp.models

class OverlayModel : ArrayList<OverlayModel.OverlayModelItem>(){
    data class OverlayModelItem(
        val created_by: Int,
        val description: String,
        val id: Int,
        val title: String
    )
}