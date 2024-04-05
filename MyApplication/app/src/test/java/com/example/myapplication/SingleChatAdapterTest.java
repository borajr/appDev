package com.example.myapplication;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class SingleChatAdapterTest {

    @Mock
    private List<ChatMessage> messagesMock;

    private SingleChatAdapter adapter;
    private String currentUserId = "user123";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ChatMessage sentMessage = new ChatMessage("Hello", true, "123", 0, currentUserId, "user456");
        ChatMessage receivedMessage = new ChatMessage("Hi", false, "124", 0, "user456", currentUserId);
        when(messagesMock.get(0)).thenReturn(sentMessage);
        when(messagesMock.get(1)).thenReturn(receivedMessage);
        when(messagesMock.size()).thenReturn(2);

        adapter = new SingleChatAdapter(messagesMock, currentUserId);
    }

    @Test
    public void getItemViewType_sentMessage_returnsCorrectType() {
        assertEquals(SingleChatAdapter.VIEW_TYPE_SENT, adapter.getItemViewType(0));
    }

    @Test
    public void getItemViewType_receivedMessage_returnsCorrectType() {
        assertEquals(SingleChatAdapter.VIEW_TYPE_RECEIVED, adapter.getItemViewType(1));
    }

    @Test
    public void onCreateViewHolder_createsCorrectViewHolderTypes() {
        RecyclerView.ViewHolder sentViewHolder = adapter.onCreateViewHolder(new RecyclerView(ApplicationProvider.getApplicationContext()), SingleChatAdapter.VIEW_TYPE_SENT);
        RecyclerView.ViewHolder receivedViewHolder = adapter.onCreateViewHolder(new RecyclerView(ApplicationProvider.getApplicationContext()), SingleChatAdapter.VIEW_TYPE_RECEIVED);

        assertEquals(SingleChatAdapter.SentMessageViewHolder.class, sentViewHolder.getClass());
        assertEquals(SingleChatAdapter.ReceivedMessageViewHolder.class, receivedViewHolder.getClass());
    }
}
