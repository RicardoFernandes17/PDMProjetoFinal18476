package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.R
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCalendarListBinding
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement

class CalendarListFragment : Fragment() {

    private var _binding: FragmentCalendarListBinding? = null
    private val binding get() = _binding!!

    var items = arrayListOf<DailyCountElement>()
    val adapter = ElementsAdapter()

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("users")
            .document(currentUser!!.uid)
            .collection("calories")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                items.clear()

                for (doc in value!!) {
                    val item = DailyCountElement.fromDoc(doc)

                    items.add(item)
                }

                adapter.notifyDataSetChanged()
            }

        binding.listViewItems.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ElementsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return items.count()
        }

        override fun getItem(position: Int): Any {
            return items[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(position: Int, parente: View?, viewGroup: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_item, viewGroup, false)

            //val rowDateTextView = rootView.findViewById<TextView>(R.id.rowDateTextView)
            val calorieNumberTextView = rootView.findViewById<TextView>(R.id.calorieNumberTextView)
            val calorieGoalTextView = rootView.findViewById<TextView>(R.id.calorieGoalTextView)
            // rowDateTextView.text = items[position].date
            calorieNumberTextView.text = "${items[position].counter}"
            calorieGoalTextView.text = "${items[position].goal}"


            return rootView
        }

    }

}