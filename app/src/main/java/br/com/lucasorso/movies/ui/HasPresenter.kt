package br.com.lucasorso.movies.ui

interface HasPresenter<MyPresenter : IPresenter> {

    val presenter: MyPresenter

}
