package com.example.kuafrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuafrapp.databinding.CommentRowBinding
import com.example.kuafrapp.model.Comment

class CommentsAdapter(private var commentList: List<Comment>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    // ViewHolder sınıfı: Tek bir yorum satırını tutar
    class CommentViewHolder(val binding: CommentRowBinding) : RecyclerView.ViewHolder(binding.root)

    // Yeni ViewHolder oluşturulduğunda çalışır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    // Yorumları bağlar
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.binding.usernameTextView.text = comment.username  // Kullanıcı adını gösterir
        holder.binding.commentTextView.text = comment.commentText  // Yorumu gösterir
    }

    // Yorum sayısı kadar çağrılır
    override fun getItemCount(): Int {
        return commentList.size
    }

    // Yeni bir yorum eklendiğinde çalışır
    fun addComment(newComment: Comment) {
        commentList = commentList + newComment
        notifyDataSetChanged()  // Listeyi güncelle
    }
}
