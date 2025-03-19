package com.example.appchat.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appchat.R;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.appchat.adapters.ChatsAdapter;
import com.example.appchat.databinding.FragmentChatsBinding;
import com.example.appchat.model.Chat;
import com.example.appchat.providers.ChatsProvider;
import com.example.appchat.view.HomeActivity;
import com.example.appchat.viewmodel.ChatViewModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private ChatViewModel chatViewModel;
    private ChatsAdapter chatsAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private String emisorId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        setupViewModel();

        return binding.getRoot();
    }

    private void setupViewModel() {

        binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        chatViewModel.getContactos().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null && !chats.isEmpty()) {
                Log.d("ChatFragment", "Número de chats: " + chats.size());

                // Actualizar chatList
                chatList.clear();
                chatList.addAll(chats);

                // Configurar el adaptador si aún no está configurado
                if (chatsAdapter == null) {
                    chatsAdapter = new ChatsAdapter(getContext(), chatList, chat -> enviarMensaje(chat));

                    binding.recyclerViewChats.setAdapter(chatsAdapter);
                } else {
                    chatsAdapter.notifyDataSetChanged();
                }

                ((HomeActivity) requireActivity()).hideProgressBar();
            } else {
                Log.d("ChatFragment", "No hay chats disponibles.");
                ((HomeActivity) requireActivity()).hideProgressBar();
            }
        });




    }





    private void enviarMensaje(Chat chat) {

        new ChatsProvider().enviarMensaje(chat.getEmisor().getUsername(), chat.getReceptor().getUsername(), chat.getMensaje());

    }


}




