package com.example.myapplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.myapplication.MainPage;
import com.example.myapplication.User;
import com.example.myapplication.arrayAdapter;

@RunWith(RobolectricTestRunner.class)
public class ArrayAdapterTest {

    @Mock
    Context mockContext;
    List<User> userList = new ArrayList<>();

    // Prepare a mock MainPage as it's needed for the adapter constructor
    @Mock
    MainPage mockMainPage;

    // Initialize the adapter
    arrayAdapter adapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        User sampleUser = new User("example@student.tue.nl", "John Doe", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTJHB2LmJDE8mRo5vCggGcP-G5Jkov0nOYt700GGxzzQg&s", "CS", "", true, true, true, 25, 160, "Male");
        userList.add(sampleUser);


        // Create instance of adapter with mocked context and sample user list
        adapter = new arrayAdapter(mockMainPage, userList);
    }

    @Test
    public void getItem_ReturnsCorrectUser() {
        // Act
        User result = adapter.getItem(0);

        // Assert
        assertThat(result.getName(), equalTo("John Doe"));
    }
}
