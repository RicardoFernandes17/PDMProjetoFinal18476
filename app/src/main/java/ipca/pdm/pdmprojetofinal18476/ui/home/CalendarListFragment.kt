package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.R
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCalendarListBinding
import ipca.pdm.pdmprojetofinal18476.helpers.LightIndicatorStatus
import ipca.pdm.pdmprojetofinal18476.helpers.getCaloriesCollection
import ipca.pdm.pdmprojetofinal18476.helpers.toShort
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement
import java.util.Date

class CalendarListFragment : Fragment() {

    private var _binding: FragmentCalendarListBinding? = null
    private val binding get() = _binding!!

    var items = arrayListOf<DailyCountElement>()
    val adapter = ElementsAdapter()

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCaloriesCollection(db, currentUser!!)
            .orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
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
            var today: String = Date().toShort()

            val rootView = layoutInflater.inflate(R.layout.row_item, viewGroup, false)

            val rowDateTextView = rootView.findViewById<TextView>(R.id.rowDateTextView)
            val calorieNumberTextView = rootView.findViewById<TextView>(R.id.calorieNumberTextView)
            val calorieGoalTextView = rootView.findViewById<TextView>(R.id.calorieGoalTextView)

            val lightCustomView =
                rootView.findViewById<DailyCountLightIndicator>(R.id.dailyCountLightIndicator)

            val burnedWithGoal = items[position].goal!! + items[position].burned!!

            rowDateTextView.text = items[position].date
            calorieNumberTextView.text = "${items[position].counter}"
            calorieGoalTextView.text = "$burnedWithGoal"

            val difference = (burnedWithGoal - items[position].counter!!).toInt()

            if (difference >= 0) {
                if (items[position].date === today && difference >= 200 || items[position].date !== today) {
                    lightCustomView.status = LightIndicatorStatus.GOOD
                } else {
                    lightCustomView.status = LightIndicatorStatus.WARNING
                }
            } else {
                lightCustomView.status = LightIndicatorStatus.BAD
            }


            return rootView
        }

    }

}