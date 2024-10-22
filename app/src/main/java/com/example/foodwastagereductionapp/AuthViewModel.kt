package com.example.foodwastagereductionapp

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState  = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    init {
        checkAuthStatus()
    }
    fun checkAuthStatus(){
        if(auth.currentUser == null){
            _authState.value = AuthState.UnAuthenticated
        }
        else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email : String , password  : String ){

        if(email.isEmpty()){
            _authState.value = AuthState.Error("Please enter your email to login")
            return
        }

        if(password.isEmpty()){
            _authState.value = AuthState.Error("Password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email , password).addOnCompleteListener{
            task -> if(task.isSuccessful){
                _authState.value  = AuthState.Authenticated
            }
            else{
                _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
            }
        }
    }


    fun signUp(email : String ,password: String , studentID: String , number : String , context :Context ){
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("Email can't be empty")
            return
        }
        if (password.isEmpty()) {
            _authState.value = AuthState.Error("Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){

                    Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        val userMap = mapOf(
                            "email" to email,
                            "studentID" to studentID,
                            "contactNumber" to number
                        )
                        firestore.collection("users").document(it)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User data added to Firestore", Toast.LENGTH_LONG).show()
                                _authState.value = AuthState.Authenticated
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
                                _authState.value = AuthState.Error(e.message ?: "There was an unexpected error from firestore")
                            }
                    }
                }
                else{
                    val errorMessage = task.exception?.message ?: "Sign up failed"
                    Toast.makeText(context, "Authentication failed: $errorMessage", Toast.LENGTH_LONG).show()
                    //Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    _authState.value = AuthState.Error(task.exception?.message ?:"Sign Up failed")

                }
            }
    }
    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}
sealed class AuthState{
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String ) : AuthState()
}