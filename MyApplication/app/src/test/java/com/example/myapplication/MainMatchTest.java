package com.example.myapplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28}) // Use the SDK version that matches your target
public class MainMatchTest {

    @Mock
    private MainPage mockMainPage;

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private Task<DocumentReference> mockTask;

    @Mock
    private DocumentReference mockDocumentReference;

    private MainMatch mainMatch;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainMatch = new MainMatch(mockMainPage);

        // Mock Firestore behavior
        Mockito.when(mockFirestore.collection(any(String.class))).thenReturn(null); // Provide the actual mock behavior
        Mockito.when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(mockTask);
        Mockito.when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);
    }

    @Test
    public void recordSwipeTest() {
        // Example test for recordSwipe method
        mainMatch.recordSwipe("user1@example.com", "user2@example.com", "right");

        // Verify that Firestore was interacted with (you may need to adjust based on your actual mock setup)
        verify(mockFirestore).collection("Swipes");

        // Add more verification/assertion as needed
    }
}
