package com.example.frameapp.models

class ToolsModel : ArrayList<ToolsModel.ToolsModelItem>(){
    data class ToolsModelItem(
        val created_by: Int,
        val description: Any,
        val id: Int,
        val img: Any,
        val layers: List<Layer>,
        val title: String
    ) {
        data class Layer(
            val created_by: Int,
            val description: String,
            val id: Int,
            val img: String,
            val overlay_id: Int,
            val title: String,
            val tool_id: Int
        )
    }
}