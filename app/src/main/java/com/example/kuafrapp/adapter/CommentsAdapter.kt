package com.example.kuafrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kuafrapp.databinding.ItemCommentBinding
import com.example.kuafrapp.model.UserComment

class CommentsAdapter : ListAdapter<UserComment, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {
    
    class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: UserComment) {
            binding.apply {
                usernameText.text = comment.username
                commentText.text = comment.commentText
                //ratingBar.rating = comment.rating.toFloat()
                //commentDate.text = comment.createdAt?.formatToString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<UserComment>() {
        override fun areItemsTheSame(oldItem: UserComment, newItem: UserComment) = 
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: UserComment, newItem: UserComment) = 
            oldItem == newItem
    }
}
