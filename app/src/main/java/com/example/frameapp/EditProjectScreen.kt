package com.example.frameapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.*
import android.graphics.Paint.Style
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easystudio.rotateimageview.RotateZoomImageView
import com.example.frameapp.adapter.CustomExpandableListAdapter
import com.example.frameapp.adapter.OverlayAdapter
import com.example.frameapp.constants.BaseClass
import com.example.frameapp.constants.Constants
import com.example.frameapp.controller.Controller
import com.example.frameapp.`interface`.GetUndoRedoSizeIF
import com.example.frameapp.`interface`.getToolId
import com.example.frameapp.`interface`.getToolType
import com.example.frameapp.models.OverlayModel
import com.example.frameapp.models.ToolsModel
import com.example.frameapp.paint.EllipseClass
import com.example.frameapp.paint.PaintConstants
import com.example.frameapp.paint.PaintView
import com.example.frameapp.paint.PaintViewCallBack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import retrofit2.Response


public class EditProjectScreen : BaseClass() ,Controller.GetOverlaysAPI,Controller.GetToolsAPI ,getToolId,
    GetUndoRedoSizeIF ,getToolType{

    lateinit var recyclerView : RecyclerView
    var adapter : OverlayAdapter? = null
    lateinit var overlayList : ArrayList<OverlayModel>
    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListDetail: HashMap<String, List<String>>? = null
    var expandableListDetail1: HashMap<String, List<String>>? = null
    private lateinit var pd: ProgressDialog
    private lateinit var undo_iv: ImageView
    private lateinit var redo_iv: ImageView
    private var relDrawData: RelativeLayout? = null
    private var ivOldScreenshot: ImageView? = null
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    var mPaintView: PaintView? = null
    var ellipse : EllipseClass? = null
    lateinit var controller: Controller
     var titleList = ArrayList<ToolsModel>()
    var subTitleList = ArrayList<ToolsModel.ToolsModelItem>()


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project_screen)

        overlayList = ArrayList()
        titleList = ArrayList()

        pd = ProgressDialog(this)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        pd.show()
        pd.setContentView(R.layout.loading)
        controller = Controller()
        controller.Controller(this,this)

        controller.getOverLays("Bearer "+getStringVal(Constants.TOKEN))
        controller.getTools("Bearer "+getStringVal(Constants.TOKEN))
        findIds()

        getToolId = this
        getUndoRedoSizeIF = this
        getToolType= this


        listeners()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun listeners() {
        mScaleGestureDetector = ScaleGestureDetector(
            applicationContext,
            ScaleListener()
        )

        mPaintView?.setPenColor(Color.WHITE)
        mPaintView?.setBackgroundResource(R.color.trans)
        mPaintView?.setCallBack(object : PaintViewCallBack {
            override fun onHasDraw() {
            }

            override fun onTouchDown() {
            }

        })

        mPaintView?.setCallBack(object : PaintViewCallBack {
            // 当画了之后对Button进行更新
            override fun onHasDraw() {
                PaintView.mCallBack.onTouchDown()
                enableUndoButton()
                disableRedoButton()
            }

            // 当点击之后让各个弹出的窗口都消失
            override fun onTouchDown() {}
        })

//        generateTv()
        undo_iv.setOnClickListener {
            onClickButtonUndo()
        }

        redo_iv.setOnClickListener {
            onClickButtonRedo()
        }

    }

    private fun onClickButtonRedo() {
        mPaintView!!.redo()
        upDateUndoRedo()
    }

    private fun onClickButtonUndo() {
        mPaintView!!.undo()
        upDateUndoRedo()
    }

    private fun upDateUndoRedo() {
        if (mPaintView!!.canUndo()) {
            enableUndoButton()
            undo_iv.setImageDrawable(resources.getDrawable(R.drawable.undoblackicon))
        } else {
            disableUndoButton()
            undo_iv.setImageDrawable(resources.getDrawable(R.drawable.undograyicon))
        }
        if (mPaintView!!.canRedo()) {
            enableRedoButton()
            redo_iv.setImageDrawable(resources.getDrawable(R.drawable.redoblackicon))
        } else {
            redo_iv.setImageDrawable(resources.getDrawable(R.drawable.redograyicon))
            disableRedoButton()
        }
    }


    private fun enableUndoButton() {

    }

    private fun disableUndoButton() {

    }

    private fun enableRedoButton() {

    }

    private fun disableRedoButton() {

    }
    private fun generateTv(id:String) {
        var Xposition = 150
        val Yposition = 500
//        for (i in texts.indices) {
            val tv = RotateZoomImageView(this)
            Picasso.get().load(id).into(tv)
            val lp = RelativeLayout.LayoutParams(250, 250)
            lp.addRule(RelativeLayout.BELOW)
            tv.layoutParams = lp
            relDrawData!!.addView(tv)
            tv.x = Xposition.toFloat()
            tv.y = Yposition.toFloat()
            tv.setOnTouchListener { v, event ->
//                pos = i

                tv.onTouch(v, event)
            }
            Xposition += 100
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            val gson = Gson()
            val strObj = data!!.getStringExtra("imageList")
            val listType = object : TypeToken<java.util.ArrayList<String?>?>() {}.type

        }
    }

    companion object {
        var getToolId : getToolId?= null
         var getUndoRedoSizeIF : GetUndoRedoSizeIF?= null
        var getToolType : getToolType? = null
    }

    private fun findIds() {
        recyclerView = findViewById(R.id.recyclerView)
        expandableListView = findViewById(R.id.expandableListView)
//        image = findViewById(R.id.image)
        redo_iv = findViewById(R.id.redo_iv)
        mPaintView = PaintView(this)
        mPaintView = findViewById(R.id.mPaintView)
        relDrawData = findViewById(R.id.relDrawData)
        undo_iv = findViewById(R.id.undo_iv)
        ivOldScreenshot = findViewById(R.id.ivOldScreenshot)
        ellipse = findViewById(R.id.ellipse)
        ellipse!!.setVisibility(View.VISIBLE);
        ellipse!!.setDrawingCacheEnabled(true);
        ellipse!!.setEnabled(true);
        ellipse!!.invalidate();

        // ivOldScreenshot.setVisibility(View.GONE);
    }

    private fun setAdapter(overlayList: OverlayModel) {
        adapter = OverlayAdapter(this,overlayList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    override fun onSuccess(overlaysResponse: Response<OverlayModel>) {
        pd.dismiss()
        if (overlaysResponse.isSuccessful)
        {
            if (overlaysResponse.code()==200)
            {
                overlayList.add(overlaysResponse.body()!!)
                setAdapter(overlaysResponse.body()!!)
            }
        } else {
            Toast.makeText(this,""+overlaysResponse.code(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessTools(toolsResponse: Response<ToolsModel>) {
            pd.dismiss()
        if (toolsResponse.isSuccessful)
        {
           if (toolsResponse.code() == 200)
           {
               for (item in toolsResponse.body()!!.indices!!)
               {
                   subTitleList = ArrayList()
                   titleList.add(toolsResponse.body()!!)
               }

               expandableListDetail = expandableListDetail1
               expandableListAdapter =
                   CustomExpandableListAdapter(this, toolsResponse.body())
               expandableListView!!.setAdapter(expandableListAdapter)
           }
        } else {
            Toast.makeText(this,""+toolsResponse.code(),Toast.LENGTH_SHORT).show()
        }
    }


    override fun onError(error: String?) {
        pd.dismiss()
        Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getToolID(id: String,colorCode:String,thickness:Int) {
        if (colorCode.length > 0)
        {
            mPaintView!!.penColor =Color.parseColor(colorCode)
            mPaintView!!.penSize = thickness

        } else if (thickness==0)
        {
            generateTv(id)
        }else {
//            mPaintView.penColor =Color.TRANSPARENT
        }
    }

    override fun getUndoRedoSize(undo: Int, redo: Int) {
        if (undo == 0) {
            undo_iv.setImageDrawable(resources.getDrawable(R.drawable.undoblackicon))
        }
    }


    override fun getToolType(type: String) {
       if (type=="1")
       {
//           mPaintView.visibility = View.VISIBLE
       } else {
//           mPaintView.penColor = Color.TRANSPARENT
       }
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        mScaleGestureDetector!!.onTouchEvent(motionEvent)
        return super.onTouchEvent(motionEvent)
    }
    private class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            mScaleFactor *= scaleGestureDetector.scaleFactor
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))
//            ivOldScreenshot.setScaleX(mScaleFactor)
//            ivOldScreenshot.setScaleY(mScaleFactor)
            return true
        }
    }
}