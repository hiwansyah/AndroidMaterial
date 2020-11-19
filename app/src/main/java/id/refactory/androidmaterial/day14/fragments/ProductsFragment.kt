package id.refactory.androidmaterial.day14.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.refactory.androidmaterial.databinding.FragmentProductsBinding
import id.refactory.androidmaterial.day14.adapters.Product
import id.refactory.androidmaterial.day14.adapters.ProductAdapter
import id.refactory.androidmaterial.day14.clients.ProductClient
import id.refactory.androidmaterial.day14.models.ProductModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment : Fragment(), ProductAdapter.ProductListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentProductsBinding
    private val adapter by lazy { ProductAdapter(requireContext(), this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false).apply {
            rvProduct.adapter = adapter
            rvProduct.layoutManager = LinearLayoutManager(requireContext())
            rvProduct.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )

            fabAdd.setOnClickListener { toProductLayout() }
        }

        ProductClient.service.getAllProduct().enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(
                call: Call<List<ProductModel>>,
                response: Response<List<ProductModel>>
            ) {
                response.body()?.let {
                    val list = mutableListOf<Product>()
                    var temp = ""

                    it.forEach { model ->
                        if (!temp.equals(model.category, true)) {
                            temp = model.category

                            list.add(Product.Header(temp))
                        }

                        list.add(Product.Row(model))
                    }

                    adapter.list = list
                }
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                onError(t)
            }
        })

        return binding.root
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()

        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSelect(id: Int, isSelect: Boolean) {

    }

    private fun toProductLayout(id: Int = 0) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragment2ToProductFragment2(
                id
            )
        )
    }
}