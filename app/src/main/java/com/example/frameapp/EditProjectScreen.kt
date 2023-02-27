package com.example.frameapp

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frameapp.adapter.CustomExpandableListAdapter
import com.example.frameapp.adapter.ExpandableListDataPump
import com.example.frameapp.adapter.OverlayAdapter
import com.example.frameapp.constants.BaseClass
import com.example.frameapp.constants.Constants
import com.example.frameapp.controller.Controller
import com.example.frameapp.databinding.ActivityMainBinding
import com.example.frameapp.models.OverlayModel
import com.example.frameapp.models.ToolsModel
import retrofit2.Response


class EditProjectScreen : BaseClass() ,Controller.GetOverlaysAPI,Controller.GetToolsAPI{

    lateinit var recyclerView : RecyclerView
    var adapter : OverlayAdapter? = null
    lateinit var overlayList : ArrayList<OverlayModel>
    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListDetail: HashMap<String, List<String>>? = null
    var expandableListDetail1: HashMap<String, List<String>>? = null
    lateinit var image : ImageView
    lateinit var viewImageDialog: Dialog
    private lateinit var pd: ProgressDialog
    lateinit var controller: Controller
     var titleList = ArrayList<ToolsModel>()
    var subTitleList = ArrayList<ToolsModel.ToolsModelItem>()


    private lateinit var binding: ActivityMainBinding

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
        listeners()
    }

    private fun listeners() {
        image.setOnClickListener {
            viewImageDialog = Dialog(this)
            viewImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            viewImageDialog.setContentView(R.layout.viewimage_dialog)
            val window = viewImageDialog.window
            window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            viewImageDialog.show()
        }
    }

    private fun findIds() {
        recyclerView = findViewById(R.id.recyclerView)
        expandableListView = findViewById(R.id.expandableListView)
        image = findViewById(R.id.image)
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
//                   for (item1 in toolsResponse.body()!!.get(item).layers.indices)
//                   {
//                       subTitleList.addAll(toolsResponse.body()!!.get(item).layers.get(item1))
//                       expandableListDetail1?.put(toolsResponse.body()!!.get(item).title,
//                           subTitleList
//                       )
//                   }
                   Log.d("tools",""+subTitleList + "   =====>   "+expandableListDetail1 )
               }

               expandableListDetail = expandableListDetail1
               expandableListAdapter =
                   CustomExpandableListAdapter(this, toolsResponse.body())
               expandableListView!!.setAdapter(expandableListAdapter)
               expandableListView!!.setOnGroupExpandListener { groupPosition ->

               }


               expandableListView!!.setOnGroupCollapseListener { groupPosition ->

               }

               expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

                   false
               }
           }
        }
    }


    override fun onError(error: String?) {
        pd.dismiss()
        Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show()
    }
}