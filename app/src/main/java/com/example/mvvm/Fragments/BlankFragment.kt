import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mvvm.R
import com.example.mvvm.User
import com.example.mvvm.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlankFragment : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var userDatabase: UserDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the UI elements
        firstNameEditText = view.findViewById(R.id.first_name)
        lastNameEditText = view.findViewById(R.id.last_name)
        emailEditText = view.findViewById(R.id.email)
        phoneNumberEditText = view.findViewById(R.id.phone_number)
        submitButton = view.findViewById(R.id.submit_button)

        // Get the database instance
        userDatabase = UserDatabase.getDatabase(requireContext())

        // Handle the submit button click
        submitButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()

            // Validate input (you can add more validation if needed)
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Insert user data into the database
                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber
                )

                // Insert the user in the background using coroutines
                lifecycleScope.launch(Dispatchers.IO) {
                    userDatabase.userDao().insert(user)
                }

                // Show success message
                Toast.makeText(requireContext(), "User data saved", Toast.LENGTH_SHORT).show()

                // Optionally, clear the fields after submission
                firstNameEditText.text.clear()
                lastNameEditText.text.clear()
                emailEditText.text.clear()
                phoneNumberEditText.text.clear()
            }
        }
    }
}
