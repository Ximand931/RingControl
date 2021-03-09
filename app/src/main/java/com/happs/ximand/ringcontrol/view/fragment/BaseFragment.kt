package com.happs.ximand.ringcontrol.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.happs.ximand.ringcontrol.BR
import com.happs.ximand.ringcontrol.view.SnackbarBuilder
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseViewModel
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding>
(private val layoutId: Int, private val menuResId: Int) : Fragment(), LifecycleOwner {

    companion object {
        @JvmStatic
        protected val MENU_NONE = 0
    }

    private var viewModel: VM? = null

    protected fun requireViewModel(): VM {
        return checkNotNull(viewModel) { "Call 'getViewModel' only after 'onCreate'" }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menuResId != MENU_NONE) {
            inflater.inflate(menuResId, menu)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setHasOptionsMenu(true)
        viewModel = createFragmentViewModel()
        lifecycle.addObserver(viewModel!!)
    }

    private fun createFragmentViewModel(): VM {
        return ViewModelProvider(
                requireActivity(), ViewModelProvider.NewInstanceFactory()
        ).get(getGenericClass())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewDataBinding: B = DataBindingUtil
                .inflate(inflater, layoutId, container, false)
        setActionBarTitle()
        onPreViewModelAttaching(requireViewModel())

        viewDataBinding.setVariable(BR.viewModel, viewModel)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        onViewDataBindingCreated(viewDataBinding)

        return viewDataBinding.root
    }

    protected fun setActionBarTitle() {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.let {
            onSetActionBarTitle(it)
        }
    }

    protected open fun onSetActionBarTitle(actionBar: ActionBar) {}

    protected open fun onViewDataBindingCreated(binding: B) {}

    protected open fun onPreViewModelAttaching(viewModel: VM) {}

    open fun onExternalEvent(eventId: Int) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireViewModel().makeSnackbarEvent.observe(
                viewLifecycleOwner, Observer { snackbarDto: SnackbarDto ->
            showSnackbarBySnackbarDto(snackbarDto)
        })
    }

    private fun showSnackbarBySnackbarDto(snackbarDto: SnackbarDto) {
        val builder = SnackbarBuilder(requireView())
                .setDuration(snackbarDto.duration)
        if (snackbarDto.actionResId != SnackbarDto.ACTION_NONE) {
            builder.setAction(
                    snackbarDto.actionResId, checkNotNull(snackbarDto.actionClickListener) {
                "Action res id is not null, but action click listener is null"
            }
            )
        }
        builder.setText(snackbarDto.messageResId)
        if (snackbarDto.iconResId != SnackbarDto.ICON_NONE) {
            builder.setIcon(snackbarDto.iconResId)
        }
        builder.snackbar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return requireViewModel().notifyOptionsMenuItemClicked(item.itemId)
    }

    private fun getGenericClass(): Class<VM> {
        val parameterizedType = Objects.requireNonNull(javaClass.genericSuperclass)
                as ParameterizedType
        @Suppress("UNCHECKED_CAST")
        return parameterizedType.actualTypeArguments[0] as Class<VM>
    }

}