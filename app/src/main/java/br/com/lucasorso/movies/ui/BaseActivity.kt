package br.com.lucasorso.movies.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.lucasorso.movies.R

abstract class BaseActivity : AppCompatActivity(), IContract {

    public override fun onResume() {
        super.onResume()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.resume()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.pause()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (this is HasPresenter<*>) {
            (this as HasPresenter<*>).presenter.destroy()
        }
    }

    override fun showMessage(message: Int, isDialog: Boolean) {
        showMessage(getString(message), isDialog)
    }

    override fun showMessage(message: String, isDialog: Boolean) {
        if (isDialog) {
            this.showToast(message)
        } else {
            this.showalertDialog(message)
        }
    }

    override fun showErrorMessage(message: String, isDialog: Boolean) {
        if (isDialog) {
            this.showalertDialog(message)
        } else {
            this.showToast(message)
        }
    }

    override fun showErrorMessage(message: Int, isDialog: Boolean) {
        showErrorMessage(getString(message), isDialog)
    }

    override fun showLoading() {
        /* Nothing */
    }

    override fun hideLoading() {
        /* Nothing */
    }

    override fun onBackOnline() {
        /* Nothing */
    }

    override fun onGotOffline() {
        /* Nothing */
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: Int) {
    showToast(this.getString(message))
}

fun Context.showalertDialog(message: String) {
    val lDialogBuilder = AlertDialog.Builder(this)
    lDialogBuilder.setTitle(R.string.atencao)
    lDialogBuilder.setMessage(message)
    lDialogBuilder.setPositiveButton(R.string.ok) { dialog, which -> dialog.dismiss() }
    lDialogBuilder.create().show()
}

fun Context.showalertDialog(message: Int) {
    showalertDialog(this.getString(message))
}

@androidx.annotation.IntRange(from = 0, to = 2)
fun Context.connectionTypeAvailabe(): Int {
    var result = 0
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = 2
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = 1
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = 2
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = 1
                }
            }
        }
    }
    return result
}