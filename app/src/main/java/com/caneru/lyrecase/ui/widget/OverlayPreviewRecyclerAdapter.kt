package com.caneru.lyrecase.ui.widget

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.caneru.lyrecase.R
import com.caneru.lyrecase.databinding.RecyclerItemOverlayPreviewBinding

open class OverlayPreviewRecyclerAdapter(
    private var overlayBitmaps: MutableList<Bitmap> = mutableListOf(),
    private var overlayNames: MutableList<String> = mutableListOf()
) :
    RecyclerView.Adapter<OverlayPreviewRecyclerAdapter.OverlayPreviewHolder>() {

    private lateinit var binding: RecyclerItemOverlayPreviewBinding
    private var clickListener: OnPreviewOverlaySelected = object : OnPreviewOverlaySelected {
        override fun onOverlaySelected(index: Int) {
            // nothing
        }
    }

    constructor(
        overlays: MutableList<Bitmap> = mutableListOf(),
        clickListener: OnPreviewOverlaySelected
    ) : this() {
        this.overlayBitmaps = overlays
        this.clickListener = clickListener
    }

    fun addItems(overlayBitmaps: List<Bitmap>, overlayNames: List<String>) {
        this.overlayBitmaps.clear()
        this.overlayNames.clear()
        this.overlayBitmaps.addAll(overlayBitmaps)
        this.overlayNames.addAll(overlayNames)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlayPreviewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_overlay_preview,
            parent,
            false
        )

        return OverlayPreviewHolder(binding)
    }

    override fun onBindViewHolder(holder: OverlayPreviewHolder, position: Int) {
        holder.bind(overlayBitmaps[position], overlayNames[position])
        holder.itemView.setOnClickListener {
            clickListener.onOverlaySelected(position)
        }
    }

    override fun getItemCount(): Int {
        return overlayBitmaps.size
    }

    open class OverlayPreviewHolder(private val binding: RecyclerItemOverlayPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap, name: String) {
            binding.ivOverlayPreview.setImageBitmap(bitmap)
            binding.tvOverlayName.text = name
        }

    }

}

interface OnPreviewOverlaySelected {
    fun onOverlaySelected(index: Int)
}