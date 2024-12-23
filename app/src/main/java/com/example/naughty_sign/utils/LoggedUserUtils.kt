@file:Suppress("UNCHECKED_CAST")

package com.example.naughty_sign.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import com.example.naughty_sign.firebase.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

//////////////////////////////////////////////////////////////////////////////////////
//⠠⢐⡀⠆⡐⠄⢢⠐⠄⢢⠐⢄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⡐⠄⢢⠐⠄⢢⠐⢄⠢⠐⡄⠢⡐⠄⢢⠐⢄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⠢⠐⡄⢂//
//⠐⢂⠰⢈⠰⠈⡔⠈⡜⠠⠌⢂⠔⠡⡐⠡⣁⠢⢁⠃⡄⢃⠡⡐⠡⣁⠢⢁⠃⠤⠑⠀⢎⡀⠎⡈⠔⢨⠀⠆⡑⢠⠑⢠⠊⢄⠊⢄⠢⠑⡠⢁⠃⡄⢃⠡⡐⠡⣁⠢⢁⠃⡄⢃⠡⡐⠡⣁⠢⢁⢃⡐⢂//
//⠈⡔⢂⠡⢂⡑⠠⠃⠤⠑⣈⠂⢌⢂⠡⠒⡀⠆⠡⠌⡐⠌⡐⠄⢃⠄⠢⣁⠊⠄⢰⣆⠀⡐⢂⠡⠊⡄⡁⢀⡄⠀⠎⡠⠘⡀⠎⢠⠂⠱⡀⠥⢈⠰⢈⠰⢀⠃⡄⠢⢁⠒⡈⠤⢁⠢⢁⠄⠢⣁⠂⠔⢂//
//⠐⠄⡊⠰⣀⠢⢁⠃⠜⡐⠠⠊⢄⠢⢁⠆⠡⠌⡁⠆⢡⠘⡀⠎⡐⠨⡁⢄⠊⠄⢸⣿⣧⠀⠌⣂⠑⡠⠀⣾⣷⠀⢁⠆⡑⠨⡐⠡⡈⢅⠰⢈⢂⠡⠊⢄⠃⢌⠠⡑⡈⠤⠑⡠⠃⢄⠃⠌⡡⠄⢊⠌⠄//
//⢈⢂⠡⢡⠀⠆⣁⠊⠔⡈⢡⠘⡀⠆⡡⢈⠆⡑⡈⢌⡐⢂⠡⢂⠡⣁⠒⡈⠔⠂⠸⣿⣿⣧⠀⠰⢈⠀⢸⣿⣿⠀⢀⠒⣈⠡⢐⠡⡐⠨⡐⡈⢄⠢⠉⡄⠊⡄⠒⡠⠁⠆⡑⢠⠑⡈⢄⢃⠰⢈⠂⡌⢂//
//⠀⠆⢡⠂⠜⡐⠠⠊⠤⠑⡠⠡⠘⡐⢠⠁⢢⠐⡁⢂⠔⡈⢄⠃⡰⠀⠆⡡⠘⠄⠀⣿⣿⣿⡆⠡⢈⠀⣿⣿⣿⠂⢀⠊⢄⠢⢁⠒⣈⠡⢐⡈⢄⠢⠑⡠⠃⢄⢃⡐⢉⠰⠈⡄⠢⢁⠢⢈⠔⢂⠡⡐⢂//
//⠈⠜⠠⠌⠒⣈⠡⢘⡀⠣⠄⡡⠃⢌⡐⠌⡐⢂⠡⠌⡐⠨⠄⢊⠄⠱⡈⠄⡃⠜⡀⠹⣿⣿⣿⡀⢂⠀⣿⣿⣿⠀⢀⠊⡄⠢⢁⠒⡠⠘⡠⠐⢂⠌⢡⠐⠡⢂⠂⡄⢃⠰⠡⢐⡁⠊⡄⠃⠌⠤⢁⠒⡠//
//⢈⠢⢁⠎⡐⠤⠘⡠⠐⡁⠆⢡⠘⢠⠐⠌⠄⡃⢄⠃⢌⠡⡘⢀⢊⠡⡐⠡⡐⠌⡐⡀⠹⣿⣿⣇⠠⠘⣿⣿⡿⠀⣀⠃⢄⠃⡌⠰⢀⢃⡐⠉⡄⢊⠄⢊⡁⠆⢡⠐⠌⡠⢃⢂⠰⢁⠰⢉⠰⢈⠂⠜⡀//
//⠀⢆⢁⠢⢐⠂⡑⢠⠑⡈⠔⠡⣈⠂⡌⢘⡐⠨⠄⢊⠄⠒⡈⠔⠈⠐⠀⠃⠔⡨⠄⣁⠂⠹⣿⣿⠀⠈⣿⣿⡇⠠⢐⡈⢂⠌⠄⡃⠌⠤⠐⣁⠒⡈⠄⢃⠰⢈⠂⡌⢂⡁⠆⢂⠌⢂⠡⢂⠌⢂⡉⠔⡠//
//⠈⡄⠢⠘⡠⠘⢠⢁⠢⢁⠊⠔⣀⠒⡈⠤⠐⡡⠘⠠⢈⣤⣶⣶⣿⣿⡿⠆⢀⣠⣤⣤⣤⣤⣌⣙⡀⠀⠻⡿⠀⢄⠃⡰⢈⠰⢈⠄⢃⠌⠒⡀⠆⡁⠎⡐⢂⠡⠌⡐⢂⠰⢈⠂⡌⢂⠡⢂⡘⢠⠐⠌⡐//
//⠐⠄⢃⠡⡐⠉⡄⠂⠆⣁⠊⠔⢠⠂⢡⠂⠱⡀⠍⠀⣾⡿⠟⠛⠉⢁⠠⠞⠛⣉⣉⣉⣛⠛⠿⢿⡿⠿⣷⣦⣤⡈⠐⠐⡌⠰⢈⠰⠈⡄⢃⠌⡐⡁⠆⠡⠌⢂⠜⠠⠌⢂⠌⡰⢀⠃⡰⢁⡐⠂⡌⢂⠡//
//⢈⡘⠠⣁⠢⠑⡠⠉⡔⠀⠎⡈⠔⣈⠂⡌⢡⠐⡀⢸⡇⠀⠀⠀⣠⣿⣷⣶⣶⡤⢩⣍⣹⡛⠻⠦⠀⠀⢸⣿⣿⣿⣷⠄⠀⠃⠌⠰⢁⠒⣈⠰⠠⠡⠌⢡⠘⡠⠌⢂⠱⢈⠰⠐⢂⠡⡐⠂⡄⠃⡄⢃⡐//
//⠠⡐⠡⡀⠆⡑⠠⠃⠤⢉⡐⢁⠒⣀⠒⡈⢄⠊⠄⢨⣷⡀⢀⣼⣿⣿⣿⣿⠏⢠⣾⣿⣿⣿⣿⠓⢶⣤⣄⣩⣿⣿⣿⠷⠀⠐⢶⣶⣶⣤⠀⠢⠡⢑⡈⠆⢂⠡⢘⠠⢂⠡⢂⡉⠤⢁⠢⠑⣀⠣⠐⢂⡐//
//⠐⡠⠑⡐⠌⡐⠡⡑⠨⠄⢂⠡⠌⡀⠆⢡⠂⠜⡀⢨⡿⠁⣾⣿⣿⡿⢁⠟⢠⣿⣿⣿⣿⣿⣿⠀⣿⣿⣿⣿⣿⣿⠟⠀⠀⠀⠈⠻⣿⣿⣷⡄⠁⢢⠐⠌⢂⠡⢂⠡⠌⢂⠡⠐⣂⠡⢂⡑⠠⢂⡉⢄⠂//
//⠠⢡⠘⢠⠘⢠⠑⠠⢃⠘⡠⢁⠎⢠⠉⠤⠘⡠⠄⠨⠃⣼⣿⣿⠟⠀⠈⠀⠈⠛⣿⠟⠋⠻⡿⠀⢹⣿⣿⡿⢿⡿⠁⠀⠀⠀⠀⠀⠙⣿⣿⣿⡆⠀⡘⠨⠄⢃⠌⡰⢈⢂⠡⠡⠄⠢⡁⢄⠃⡔⠠⢊⠐//
//⠠⡁⢌⠂⠜⠠⠌⡁⠆⢡⠒⢠⠘⢠⠘⣀⠣⢐⠀⠀⢸⣿⣿⣿⠂⢀⣼⠇⠓⠀⠀⢠⣶⣷⠄⣈⡈⠻⣿⠃⣸⠃⠀⠀⠀⠀⠀⠀⠀⠈⣿⡿⠁⢀⠰⢁⠊⡄⠒⢠⠂⢌⠰⠁⡌⢡⠐⡈⠔⣀⠣⠄⢃//
//⠐⡈⢄⠊⢌⠂⠥⢈⠔⠡⣈⠂⠜⣀⠒⢠⠂⠡⠀⡀⣹⣿⣿⣅⠀⠘⠻⠀⠚⠙⣷⡀⢘⠟⣰⣿⡇⢱⣤⠀⣧⣄⠀⢀⣸⡄⢀⣀⣤⣶⡿⠁⠀⠆⢌⠂⡡⠐⡉⠤⠘⡀⠆⡑⡐⢂⠡⠌⢂⠔⠠⢊⠄//
//⠠⢁⠆⡘⢠⠘⢠⠊⠄⢃⠤⠘⡠⢂⠘⡀⠤⢶⣿⣇⠸⡇⢻⡏⠀⡇⠀⠀⠀⢲⣤⣤⡈⠀⠛⠿⢿⡘⡇⣸⣿⡏⢠⣿⣿⣿⣿⣿⣿⠛⠀⢄⠩⡈⠔⡈⢄⠃⢌⡐⠡⠌⠰⢐⠠⢃⠰⢈⠂⡌⢁⠆⠌//
//⢀⠃⡰⠈⡄⢊⠤⠘⡈⢄⠢⠑⡐⡀⠐⣻⠂⠀⡀⡀⠀⠁⠀⠳⠀⠒⣠⣦⣤⣿⣿⣿⣧⡄⠀⡀⠀⢀⠀⢉⡙⠃⣼⣿⣿⣿⣿⠋⠀⠤⠘⡀⠆⡁⠆⢡⠂⠜⢠⢀⠃⡌⠡⢂⠡⢂⠌⢂⠡⡐⠌⠰⡈//
//⠠⢁⠂⠥⠐⡂⢄⠃⢌⡐⢂⠱⠐⡠⠀⠳⠀⠠⠑⣈⠒⠀⣠⡄⠀⢸⣿⣿⣿⣿⣿⣿⣿⣷⣶⣤⡠⠁⣴⡿⠃⢰⣿⣿⣿⠟⠀⢠⠉⡄⢃⠌⡐⠡⡘⢠⠘⡐⠂⡌⠰⢀⠃⡌⡐⢂⠌⢂⠡⡐⠌⢡⠐//
//⢀⠃⢌⠂⡑⡈⢄⠊⠤⢐⠨⠄⢃⠰⡀⠄⠀⠀⢃⠄⢊⠄⡀⠃⠐⡀⠙⠻⣿⣦⣉⣥⣿⣿⣿⠋⢁⠘⠁⠀⢠⣿⣿⣿⠁⠠⢈⢂⠒⡐⠨⡐⢈⠡⡐⢁⠢⠌⢡⠐⣁⠊⠰⠐⡠⠃⠌⢂⠡⡐⠌⢂⠌//
//⠀⠎⡀⠎⡐⡈⢄⠊⠔⡈⠔⣈⠂⠥⠐⢢⠐⠌⢂⠜⠠⡘⢀⠆⢠⠐⠡⢂⠀⠉⢉⠉⠉⠀⠀⠀⠛⠠⠆⠀⠀⠉⠛⢿⣦⠀⢁⠢⠌⡐⢡⠐⠡⢂⠡⠌⡐⠌⡐⢂⠤⢁⠃⡡⠐⡁⠎⢠⢁⠒⡈⠆⡈//
//⠈⡔⢁⠢⢁⠔⡈⢄⠃⢌⠰⠀⠎⡠⢉⠄⢊⠌⢂⠌⢂⠡⢂⡘⠄⢊⡁⠆⠁⢰⡏⠀⠀⠀⠀⠀⠤⢀⣾⣿⣶⠈⠄⠀⡌⢁⠀⠂⡜⠠⡁⠈⢁⠆⢡⠂⡡⠘⠠⠌⡐⠨⠄⡡⠑⡈⠔⡁⠢⠌⡐⠌⡐//
//⠐⢄⠊⡐⠌⡐⠨⠄⢊⠄⢊⡁⠆⡁⠆⡘⢠⠘⡀⠎⡠⠑⢂⠰⢈⠂⡔⢈⠂⠈⠇⠀⠀⠀⠀⢠⢠⡿⣿⣿⣿⡇⠀⣰⠛⠈⠀⠐⡄⠃⠄⠀⡀⠎⡀⠆⢡⠁⣃⠘⢠⠑⡈⠤⠑⡈⠔⡈⡁⠆⢡⠘⠠//
//⠈⡄⢊⡐⠌⡐⢡⠘⡀⠎⢠⠐⠌⡐⠌⠤⢁⠢⢁⠆⢡⠘⡠⢁⠆⢡⠐⠌⣂⠁⠠⣷⡄⠀⣠⡾⠀⠧⢹⣯⣅⣀⠀⠀⠀⠀⠀⠀⠐⠁⠀⡄⢂⠥⠘⡈⢄⠊⢄⡘⠄⠢⢁⠆⡑⠨⠄⡑⠰⢈⠂⡌⠡//
//⠐⡠⠡⢐⠨⠐⡂⢌⠰⢈⠂⠜⢠⢁⠊⠔⡈⠔⠂⡌⠂⡔⢁⠢⠌⢂⠌⠒⡀⠀⠀⠈⠁⠀⠁⠀⠀⠀⠈⣿⣿⣿⠆⠀⠀⠀⠀⠀⠀⠀⢡⠐⠌⡠⠑⡈⢄⠊⠤⢐⡈⠅⡂⠒⣈⠁⠆⡡⠘⡠⢁⠢⢁//
//⠠⢁⠆⠡⢂⡑⠰⢈⠰⠈⢌⢂⡁⠢⢘⠠⣁⠊⠔⡠⢃⠰⢈⠰⢈⠂⡌⢡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿⡇⠀⠀⠀⠀⠀⠀⠐⡂⢌⠂⢅⡘⠤⢈⠂⡅⠢⠐⡌⢐⠡⠄⢊⡐⠁⢆⠁⠆⢡⠂//
//⢀⠃⠌⣂⠡⠄⢃⠌⠤⠉⡄⢂⠰⢁⠢⢁⠄⢊⠔⡐⢠⢁⠊⡰⠈⡔⠠⣁⠂⠄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣿⡀⠀⠀⠀⠀⠀⡀⠰⡈⠌⠤⠐⢂⠡⠌⡠⢁⢃⠰⢈⠰⢈⠂⠤⠉⡄⢊⠌⡐⠌//
//⠀⠎⡐⠄⢒⢈⠂⡌⠰⢁⠄⢃⠰⢈⠰⠈⡄⢃⠰⠈⡄⠢⢡⠐⠡⠄⢃⠄⢊⠔⡐⠄⡀⢤⣈⣀⡀⠀⠀⠀⢹⣿⣿⡇⠀⠀⠀⠀⠀⠌⠡⠀⠁⠘⡈⢄⠃⡰⢁⠢⢈⠔⡈⠔⡈⢌⠰⠁⡔⢈⠰⠈⡔//
//⠈⡔⢈⠌⢂⠌⡰⢀⠃⡌⠰⢈⠄⢃⠰⠡⡐⠌⡠⠃⢄⢃⠰⢈⠡⡘⡀⠎⢠⠒⡈⠔⡀⠘⣿⣿⣿⡀⠂⡄⠸⣿⣿⣇⠀⠀⡀⠆⡀⢀⠀⢀⢂⡑⠨⠄⢊⡐⠤⠁⠆⠒⡈⠤⢁⠢⠌⠒⣀⠃⡰⠡⠐//
//⠐⡰⢈⠰⢈⠰⢀⠆⢡⠐⣁⠊⠰⡈⠤⠑⡠⠊⢄⠱⠈⡄⠢⢁⠒⢠⠡⠘⡀⠆⢡⠘⣀⠂⢹⡿⠟⠁⠐⡀⠀⣿⣿⣿⠀⠀⠔⡨⠄⠃⡄⠀⠀⠄⢃⡘⠄⡐⢂⠩⠐⡡⠌⠰⡈⠰⢈⠡⠄⢊⠄⡡⠃//
//⠐⢄⠢⠡⠌⢂⠆⡈⠆⢌⠠⠌⢡⠐⢂⡑⠠⡑⡈⠄⡃⠄⡃⠌⢌⡐⠄⡃⢌⠘⣀⠒⢠⠂⠀⠀⠀⠀⠀⡐⠀⢹⣿⣿⡇⠀⡘⠠⠌⢡⠐⠄⡂⠜⢠⠐⠌⠄⡃⠌⡡⢐⡈⠅⡰⢁⠊⡐⠌⢂⠔⡠⢁//
//⠈⡄⠢⢡⠘⡀⠆⠡⠌⢂⢂⡉⢄⠊⢄⢂⡑⠤⠘⡐⡈⠔⡈⠔⢂⠰⢈⡐⢂⠡⡐⠌⢂⠜⡀⠁⠀⠀⠁⠠⠁⣘⠛⠛⡡⠀⠐⡁⠎⡀⠎⡐⠡⠌⢂⠌⡘⡐⡈⠔⡠⠡⠐⡌⠐⠤⠑⡈⠔⡁⢢⠐⠡//
//⠐⠄⣃⠐⢂⠡⠌⢡⠘⢠⠂⡐⢂⠌⢂⠄⢢⠐⣁⠂⢅⠢⢁⠜⡀⠎⢠⠐⠌⡐⠰⢈⠢⠐⠤⠀⣀⣠⣴⡀⠄⠰⠶⠶⠒⠂⢀⠰⠈⠔⢂⠡⢊⡐⡁⠢⢁⠔⡈⠔⡠⢁⠃⠤⠉⡄⠃⡌⠰⢈⠄⢊⠡//
//⢈⠢⠐⣈⠂⠥⠘⡠⠘⡀⠆⠡⠌⠰⡈⡐⢂⠌⢄⡘⠠⢂⠡⢂⠌⠰⠁⡌⢂⠡⡁⠆⢡⠉⠀⣰⣿⣿⣿⡇⠀⢠⣤⣤⣴⠀⠠⠌⡑⣈⠂⢅⠂⡔⢈⠡⢂⠒⡈⠔⡠⢁⢊⠰⢁⠰⢁⠰⢁⠢⠘⡠⢁//
//⠀⠆⡑⢠⠘⣀⢃⠰⠁⡌⠌⠡⠌⢡⠐⣁⠢⠌⡠⠐⡡⠌⢂⠆⢨⠁⠎⡐⠨⡐⠤⠉⡄⠊⠄⢸⣿⣿⠃⠀⠀⢸⣿⣿⡟⠀⠄⣃⠰⠠⢘⡀⠎⡐⡈⠔⢂⠡⠌⠰⠠⣁⠢⠌⢂⠡⠌⢂⠌⡰⠁⡔⠡//
//⢈⠂⢅⠂⢒⡀⠆⠌⠒⢠⠊⢡⠘⣀⠒⡀⠆⢂⠡⡁⠆⡘⠠⠌⢂⡘⢐⡈⢡⠐⠂⡅⢂⠩⠐⡀⠙⠈⠀⡈⠄⠈⠉⠿⠃⠠⢘⠀⠆⡑⢂⡐⢂⠡⡐⠌⢂⠌⢂⠥⢁⠄⠒⡨⠄⠃⡌⢂⠒⢠⢁⠢⢁//
//⢀⠊⠄⠊⡄⠰⡈⠌⡑⢂⡘⠄⢒⡀⠆⢡⠘⡠⢁⠒⡈⠔⠡⡘⢠⠐⢂⠌⡐⠨⡁⠔⡨⠐⡡⠘⡀⠂⠑⡈⠜⡀⠆⠰⠈⠔⠡⣈⠒⡈⠤⠐⡌⡐⠤⠉⡄⢊⠄⠒⡈⢄⠃⡔⢈⠡⡐⠌⡰⢁⠂⠆⡡//
//////////////////////////////////////////////////////////////////////////////////////

/**
 * Clase que centraliza las operaciones relacionadas con el usuario de la aplicación.
 */
object LoggedUserUtils {

    /**
     * Acceso a la base de datos de la aplicación.
     */
    @SuppressLint("StaticFieldLeak")
    private val database = Firebase.firestore

    /**
     * Encargado de la autentificación de Firebase
     */
    private val firebaseAuth = Firebase.auth


    /**
     * Obtiene el email del usuario y lo devuelve en forma de cadena.
     */
    fun obtenerEmail(): String = firebaseAuth.currentUser?.email.toString()

    /**
     * Obtiene el uid y lo devuelve en forma de cadena.
     */
    fun obtenerUid(): String = firebaseAuth.currentUser?.uid.toString()

    /**
     * Actualiza un dato especificado y lo sutituye por otro.
     */
    fun actualizar(campo: String, valor: Any?) {
        val collection = database.collection("Usuarios").document(obtenerUid())

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER

        // Get the document, forcing the SDK to use the offline cache
        collection.get(source).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result
                if (document != null && document.exists()) {
                    // Actualizar el campo "ciudad" en el documento
                    document.reference.update(campo, valor)
                        .addOnSuccessListener {
                            Log.d(TAG, "$campo updated to: $valor")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error updating $campo", e)
                        }
                } else {
                    Log.w(TAG, "Document doesn't exist.")
                }

            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
        }
    }

    /**
     * Crea un documento en la base de datos que será usado por el usuario a partir de ahora.
     */
    fun creacionDocumentoUsuario(bundle: Bundle) {
        // ------ { Se accede a la colección donde se almacenan los usuarios } ------ //
        val collection = database.collection("Usuarios")
        collection.get().addOnSuccessListener { _ ->
            val userDocument = crearUsuario(bundle, collection)
            creacionDocumentoLikes(userDocument)
            creacionDocumentoMatches(userDocument)
        }
    }

    /**
     * Crea una nueva colección donde se almacenarán los matches de un usuario designado por parámetro.
     */
    private fun creacionDocumentoMatches(userDocument: DocumentReference) {
        // ------ { Creación de una nueva colección de Matches  } ------ //
        val matchesCollection = userDocument.collection("Matches")
        matchesCollection.get().addOnSuccessListener { _ ->
            val uuidDocument = matchesCollection.document("UUID")
            uuidDocument.set(mapOf(Pair("UUID", listOf(""))))
        }
    }

    /**
     * Crea una nueva colección donde se almacenarán los likes de un usuario designado por parámetro.
     */
    private fun creacionDocumentoLikes(userDocument: DocumentReference) {
        // ------ { Creación de una nueva colección de Likes  } ------ //
        val likesCollection = userDocument.collection("Likes")
        likesCollection.get().addOnSuccessListener { _ ->
            val uuidDocument = likesCollection.document("UUID")
            uuidDocument.set(mapOf(Pair("UUID", listOf(""))))
        }
    }

    /**
     * Crea un nuevo documento en la base de datos asociado al usuario registrado.
     */
    private fun crearUsuario(
        bundle: Bundle,
        collection: CollectionReference
    ): DocumentReference {
        // ------ { Formación de los datos del usuario } ------ //
        val user = User(
            bundle.getString("id"),
            bundle.getString("email"),
            bundle.getString("nombre"),
            bundle.getString("cita"),
            bundle.getString("profesion"),
            bundle.getString("ciudad"),
            bundle.getString("descripcion"),
            listOf(),
            "",
            "",
            bundle.getInt("edad"),
        )
        val userDocument = collection.document(bundle.getString("id").toString())
        userDocument.set(user)
        return userDocument
    }

    /**
     * Almacena el interés del usuario en la base de datos.
     */
    fun almacenarLike(uuid: String) {
        // ------ { Se extrae el documento del usuario actual y la colección de Likes } ------ //
        val documentReference = database.collection("Usuarios").document(obtenerUid())
        documentReference.collection("Likes").document("UUID").get()
            .addOnSuccessListener { documentSnapshot ->
                val listaUUIDs =
                    documentSnapshot.get("UUID") as ArrayList<String>
                if (!listaUUIDs.contains(uuid)) {
                    listaUUIDs.add(uuid)
                    documentReference.collection("Likes").document("UUID")
                        .update("UUID", listaUUIDs)
                    Log.d("SEXO", listaUUIDs.toString())
                }
            }
    }

    /**
     * Se obtiene la lista de likes del usuario almacenado en la base de datos. Esta versión usa
     * coroutines para manejar la asincronía de manera más eficiente.
     */
    suspend fun extraerListaLikes(): List<User> {
        val listaUsuarios = mutableListOf<User>()
        // ------ { Se extrae el documento del usuario actual y la colección de Likes } ------ //
        val documentReference = database.collection("Usuarios").document(obtenerUid())
        try {
            val documentSnapshot =
                documentReference.collection("Likes").document("UUID").get().await()
            listaUsuarios.add(extraerUsuarioPorUUID(documentSnapshot.get("UUID").toString()))
        } catch (e: Exception) {
            println("Error al obtener los datos: ${e.message}")
            return ArrayList()
        }
        return listaUsuarios
    }

    /**
     * Extrae los datos almacenados del perfil actual y los devuelve en forma de Bundle.
     * Esta versión usa coroutines para manejar la asincronía de manera más eficiente.
     */
    suspend fun extraerDatosPerfil(): Bundle {
        val collection = database.collection("Usuarios").document(obtenerUid())
        val source = Source.SERVER
        val bundle = Bundle()

        try {
            // Obtener el documento de manera asincrónica
            val documentSnapshot = collection.get(source).await()

            if (documentSnapshot.exists()) {
                // Si el documento existe, se agregan los datos al Bundle
                bundle.apply {
                    putString("nombre", documentSnapshot.getString("nombre"))
                    putString("cita", documentSnapshot.getString("cita"))
                    putString("descripcion", documentSnapshot.getString("descripcion"))
                    putString("profesion", documentSnapshot.getString("profesion"))
                    putString("ciudad", documentSnapshot.getString("ciudad"))
                    @Suppress("UNCHECKED_CAST")
                    putStringArrayList(
                        "intereses",
                        documentSnapshot.get("intereses") as ArrayList<String>
                    )
                }
            } else {
                Log.w(TAG, "Error: El documento no existe")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: No se pudieron obtener los datos: ", e)
        }
        return bundle
    }

    suspend fun extraerUsuarioPorUUID(uuid: String): User {
        val collection = database.collection("Usuarios").document(uuid)
        val source = Source.SERVER
        lateinit var user: User

        try {
            // Obtener el documento de manera asincrónica
            val documentSnapshot = collection.get(source).await()
            if (documentSnapshot.exists()) {
                // Si el documento existe, se agregan los datos al Bundle
                user = User(
                    documentSnapshot.getString("id"),
                    documentSnapshot.getString("email"),
                    documentSnapshot.getString("nombre"),
                    documentSnapshot.getString("cita"),
                    documentSnapshot.getString("profesion"),
                    documentSnapshot.getString("ciudad"),
                    documentSnapshot.getString("descripcion"),
                    listOf(),
                    documentSnapshot.getString("foto_perfil"),
                    documentSnapshot.getString("ubicacion"),
                    Integer.parseInt(documentSnapshot.getString("edad").toString()),
                )
            } else {
                Log.w(TAG, "Error: El documento no existe")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: No se pudieron obtener los datos: ", e)
        }
        return user
    }

    /**
     * Cierra la sesión del usuario y limpia la caché.
     */
    fun cerrarSesionUsuario() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            FirebaseFirestore.getInstance().clearPersistence()
        }
    }
}