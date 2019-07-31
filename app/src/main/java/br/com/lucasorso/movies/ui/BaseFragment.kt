package br.com.lucasorso.movies.ui

import android.content.IntentFilter
import android.os.Build
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.broadcast.NetworkChangeReceiver
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment(), IContract, NetworkChangeReceiver.Listener {

    private var firstVerifyInternet: Boolean = true
    private val networkReceiver by lazy { NetworkChangeReceiver() }

    override fun onResume() {
        super.onResume()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.resume()
        }
    }

    override fun onStart() {
        super.onStart()
        firstVerifyInternet = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            activity?.registerReceiver(networkReceiver, filter)
        }
        NetworkChangeReceiver.setConnectivityListener(this)
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity?.unregisterReceiver(networkReceiver)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.destroy()
        }
    }

    override fun showMessage(message: String, isDialog: Boolean) {
        if (isDialog) {
            activity?.showalertDialog(message)
        } else {
            activity?.showToast(message)
        }
    }

    override fun showMessage(message: Int, isDialog: Boolean) {
        showMessage(getString(message), isDialog)
    }

    override fun showErrorMessage(message: String, isDialog: Boolean) {
        if (isDialog) {
            activity?.showalertDialog(message)
        } else {
            activity?.showToast(message)
        }
    }

    override fun showErrorMessage(message: Int, isDialog: Boolean) {
        showErrorMessage(getString(message), isDialog)
    }

    override fun onConnectionChange(hasInternet: Boolean) {
        if (firstVerifyInternet) {
            firstVerifyInternet = false
            return
        }
        if (hasInternet) {
            onBackOnline()
        } else {
            onGotOffline()
        }
    }

    override fun showLoading() {
        /* Nothing */
    }

    override fun hideLoading() {
        /* Nothing */
    }

    override fun onBackOnline() {
        view?.let {
            val snack = Snackbar.make(it, R.string.got_connected,
                    Snackbar.LENGTH_SHORT)
            val view = snack.view
            val tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            tv.setTextColor(ContextCompat.getColor(it.context, R.color.greenSnack))
            snack.show()
        }
    }

    override fun onGotOffline() {
        view?.let {
            val snack = Snackbar.make(it, R.string.has_been_disconnected,
                    Snackbar.LENGTH_SHORT)
            val view = snack.view
            val tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            tv.setTextColor(ContextCompat.getColor(it.context, R.color.redSnack))
            snack.show()
        }
    }

}