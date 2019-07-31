package br.com.lucasorso.movies.ui

interface IContract {

    fun showLoading()

    fun hideLoading()

    fun onBackOnline()

    fun onGotOffline()

    fun showMessage(message: Int, isDialog: Boolean = false)

    fun showMessage(message: String, isDialog: Boolean = false)

    fun showErrorMessage(message: Int, isDialog: Boolean = false)

    fun showErrorMessage(message: String, isDialog: Boolean = false)

}
