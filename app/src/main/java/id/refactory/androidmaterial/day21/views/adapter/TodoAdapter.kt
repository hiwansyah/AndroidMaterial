package id.refactory.androidmaterial.day21.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.refactory.androidmaterial.R
import id.refactory.androidmaterial.databinding.ItemTodoNewBinding
import id.refactory.androidmaterial.day21.models.TodoModel

class TodoAdapter(
    private val context: Context, private val listener: TodoListener
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    var list = mutableListOf<TodoModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun insertTodo(todoModel: TodoModel) {
        list.add(0, todoModel)
        notifyItemInserted(0)
    }

    fun updateTodo(todoModel: TodoModel) {
        val index = list.indexOfFirst { it.id == todoModel.id }
        if (index != -1) {
            list[index] = todoModel
            notifyItemChanged(index)
        }
    }

    fun deleteTodo(todoModel: TodoModel) {
        val index = list.indexOfFirst { it.id == todoModel.id }
        if (index != -1) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    interface TodoListener {
        fun onChange(todoModel: TodoModel)
        fun onDelete(todoModel: TodoModel)
        fun onFavorite(todoModel: TodoModel)
    }

    inner class ViewHolder(val binding: ItemTodoNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(todoModel: TodoModel) {
            binding.run {
                tvTodo.text = todoModel.task
                ivStatus.setImageResource(if (todoModel.status) R.drawable.ic_done else R.drawable.ic_todo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTodoNewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model by lazy { list[position] }

        holder.bindData(model)
        holder.binding.run {
            cvRoot.setOnClickListener { listener.onFavorite(model) }
            ivStatus.setOnClickListener { listener.onChange(model) }
            ivTodo.setOnClickListener { listener.onDelete(model) }
        }
    }

    override fun getItemCount(): Int = list.size
}