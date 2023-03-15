package com.example.frameapp.models

class ToolsModel : ArrayList<ToolsModelItem>(){
    data class ToolsModelItem(
        val created_by: Int,
        val description: Any,
        val id: Int,
        val img: Any,
        val layers: List<Layer>,
        val title: String
    ) {
        data class Layer(
            val colorCode: String,
            val created_by: Int,
            val description: String,
            val height: Any,
            val heightType: Any,
            val id: Int,
            val img: Any,
            val overlay_id: Int,
            val shapeImage: String,
            val thickness: Int,
            val thicknessType: String,
            val title: String,
            val tool_id: Int,
            val type: String,
            val width: Any,
            val widthType: Any
        )
    }
}