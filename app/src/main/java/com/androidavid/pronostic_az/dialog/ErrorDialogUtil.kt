package com.androidavid.pronostic_az.dialog

import android.app.AlertDialog
import android.content.Context


class ErrorDialogUtil {

    companion object {
        fun showNoInternetConnectionErrorDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Error de conexión")
                .setMessage("No hay conexión a Internet. Por favor, verifica tu conexión e inténtalo nuevamente.")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Puedes agregar más funciones para manejar otros tipos de errores
    }
}