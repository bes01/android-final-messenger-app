package ge.bkapa.tkats.messengerapp.view

import androidx.fragment.app.Fragment

interface FragmentedActivity {

    fun getFragment(index:Int):Fragment

}