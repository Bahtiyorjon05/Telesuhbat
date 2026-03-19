package com.radolyn.ayugram.proprietary;

import android.text.TextUtils;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pair;
import com.google.android.exoplayer2.util.Log;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import com.radolyn.ayugram.messages.AyuMessagesController;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

public abstract class AyuHistoryHook {
    public static void doHook(
        int currentAccount,
        ArrayList<MessageObject> messArr,
        SparseArray<MessageObject>[] messagesDict,
        int startId,
        int endId,
        long dialogId,
        int limit,
        int topicId,
        boolean isSecretChat
    ) {
        Iterator<MessageObject> it;
        Iterator<TLRPC.Chat> it2;
        MessagesStorage messagesStorage = MessagesStorage.getInstance(currentAccount);
        long currentClientUserId = UserConfig.getInstance(currentAccount).clientUserId;
        AyuMessagesController ayuMessagesController = AyuMessagesController.getInstance();
        LongSparseArray<TLRPC.Message> longSparseArray = new LongSparseArray<>();
        ArrayList<Long> messageGroupsIds = new ArrayList<>();
        ArrayList<Integer> replyToMessageIds = new ArrayList<>();
        List<DeletedMessageFull> deletedMessages = ayuMessagesController.getMessages(currentClientUserId, dialogId, topicId, startId, endId, limit);

        if (deletedMessages.isEmpty()) {
            return;
        }

        ArrayList<Long> usersToLoad = new ArrayList<>();
        ArrayList<Long> chatsToLoad = new ArrayList<>();

        for (DeletedMessageFull deletedMessageFull : deletedMessages) {
            if (!TextUtils.isEmpty(deletedMessageFull.message.text)
                || !TextUtils.isEmpty(deletedMessageFull.message.mediaPath)
                || deletedMessageFull.message.documentSerialized != null) {

                TLRPC.TL_message map = map(deletedMessageFull, currentAccount);
                long groupedMessagesId = map.grouped_id;

                if (groupedMessagesId != 0) {
                    messageGroupsIds.add(Long.valueOf(groupedMessagesId));
                }

                TLRPC.TL_messageReplyHeader tLRPC$TL_messageReplyHeader = map.reply_to;
                if (tLRPC$TL_messageReplyHeader != null) {
                    replyToMessageIds.add(Integer.valueOf(tLRPC$TL_messageReplyHeader.reply_to_msg_id));
                }

                longSparseArray.put(map.id, map);
                MessagesStorage.addUsersAndChatsFromMessage(map, usersToLoad, chatsToLoad, null);
            }
        }

        Iterator<Long> it4 = messageGroupsIds.iterator();
        while (it4.hasNext()) {
            for (DeletedMessageFull deletedMessageFull2 : ayuMessagesController.getMessagesGrouped(currentClientUserId, dialogId, ((Long) it4.next()).longValue())) {
                if (!TextUtils.isEmpty(deletedMessageFull2.message.text)
                    || !TextUtils.isEmpty(deletedMessageFull2.message.mediaPath)
                    || deletedMessageFull2.message.documentSerialized != null) {

                    if (!longSparseArray.containsKey(deletedMessageFull2.message.messageId)) {
                        TLRPC.TL_message map2 = map(deletedMessageFull2, currentAccount);
                        TLRPC.TL_messageReplyHeader tLRPC$TL_messageReplyHeader2 = map2.reply_to;

                        if (tLRPC$TL_messageReplyHeader2 != null) {
                            replyToMessageIds.add(Integer.valueOf(tLRPC$TL_messageReplyHeader2.reply_to_msg_id));
                        }

                        longSparseArray.put(map2.id, map2);
                        MessagesStorage.addUsersAndChatsFromMessage(map2, usersToLoad, chatsToLoad, null);
                    }
                }
            }
        }

        Iterator<Integer> it5 = replyToMessageIds.iterator();
        while (it5.hasNext()) {
            DeletedMessageFull message = ayuMessagesController.getMessage(currentClientUserId, dialogId, ((Integer) it5.next()).intValue());
            if (message != null
                && (!TextUtils.isEmpty(message.message.text)
                    || !TextUtils.isEmpty(message.message.mediaPath)
                    || message.message.documentSerialized != null)) {

                if (!longSparseArray.containsKey(message.message.messageId)) {
                    TLRPC.TL_message map3 = map(message, currentAccount);
                    longSparseArray.put(map3.id, map3);
                    MessagesStorage.addUsersAndChatsFromMessage(map3, usersToLoad, chatsToLoad, null);
                }
            }
        }

        ArrayList<TLRPC.User> someUsersFrom_usersAndChatsFromDeletedMessages = new ArrayList<>();
        ArrayList<TLRPC.Chat> deletedChats = new ArrayList<>();

        try {
            if (!usersToLoad.isEmpty()) {
                messagesStorage.getUsersInternal(TextUtils.join(",", usersToLoad), someUsersFrom_usersAndChatsFromDeletedMessages);
            }
        } catch (Exception e2) {
            Log.e("AyuGram", String.valueOf(e2));
        }

        try {
            if (!chatsToLoad.isEmpty()) {
                messagesStorage.getChatsInternal(TextUtils.join(",", chatsToLoad), deletedChats);
            }
        } catch (Exception e3) {
            Log.e("AyuGram", String.valueOf(e3));
        }

        LongSparseArray<TLRPC.User> newMessageObjectUsers = new LongSparseArray<>();
        LongSparseArray<TLRPC.Chat> newMessageObjectChats = new LongSparseArray<>();

        it = someUsersFrom_usersAndChatsFromDeletedMessages.iterator();
        while (it.hasNext()) {
            TLRPC.User next = it.next();
            newMessageObjectUsers.put(next.id, next);
        }

        it2 = deletedChats.iterator();
        while (it2.hasNext()) {
            TLRPC.Chat next2 = it2.next();
            newMessageObjectChats.put(next2.id, next2);
        }

        Comparator<MessageObject> comparator2 = new Comparator<MessageObject>() {
            @Override
            public final int compare(MessageObject obj, MessageObject obj2) {
                return AyuHistoryHook.doHook_compareMessages(obj, obj2);
            }
        };

        if (messArr.size() > 1) {
            if (isSecretChat) {
                comparator2 = comparator2.reversed();
            }

            for (int i6 = 0; i6 < longSparseArray.size(); i6++) {
                messArr.add(new MessageObject(
                    currentAccount,
                    (TLRPC.Message) longSparseArray.get(longSparseArray.keyAt(i6)),
                    newMessageObjectUsers,
                    newMessageObjectChats,
                    false,
                    true
                ));
            }

            Iterator<MessageObject> it3 = messArr.iterator();
            while (it3.hasNext()) {
                MessageObject messageObject2 = it3.next();
                TLRPC.TL_messageReplyHeader tLRPC$TL_messageReplyHeader3 = messageObject2.messageOwner.reply_to;

                if (tLRPC$TL_messageReplyHeader3 != null) {
                    MessageObject messageObject3 = (MessageObject) messagesDict[0].get(tLRPC$TL_messageReplyHeader3.reply_to_msg_id);
                    if (messageObject3 == null) {
                        messageObject3 = (MessageObject) messagesDict[1].get(messageObject2.messageOwner.reply_to.reply_to_msg_id);
                    }

                    if (messageObject3 == null) {
                        Iterator<MessageObject> it7 = messArr.iterator();
                        while (true) {
                            if (!it7.hasNext()) {
                                break;
                            }
                            MessageObject messageObject4 = it7.next();
                            if (messageObject2.messageOwner.reply_to != null
                                && messageObject4.getId() == messageObject2.messageOwner.reply_to.reply_to_msg_id) {
                                messageObject3 = messageObject4;
                                break;
                            }
                        }
                    }

                    if (messageObject3 != null) {
                        messageObject2.messageOwner.replyMessage = messageObject3.messageOwner;
                        messageObject2.replyMessageObject = messageObject3;
                    }
                }
            }

            Collections.sort(messArr, comparator2);
        }
    }

    public static Pair<Integer, Integer> getMinAndMaxIds(ArrayList<MessageObject> arrayList) {
        Iterator<MessageObject> it = arrayList.iterator();
        int minId = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int maxId = Integer.MIN_VALUE;

        while (it.hasNext()) {
            MessageObject messageObject = (MessageObject) it.next();
            if (!messageObject.isSending()) {
                int id = messageObject.getId();
                if (id < minId) {
                    minId = id;
                }
                if (id > maxId) {
                    maxId = id;
                }
            }
        }

        return new Pair<>(minId, maxId);
    }

    private static int doHook_compareMessages(MessageObject a, MessageObject b) {
        int id = a.getId();
        int id2 = b.getId();
        int i = a.messageOwner.date;
        int i2 = b.messageOwner.date;

        if (id > 0 && id2 > 0) {
            if (id > id2) {
                return -1;
            }
            return id < id2 ? 1 : 0;
        } else if (id >= 0 || id2 >= 0) {
            if (i > i2) {
                return -1;
            }
            return i < i2 ? 1 : 0;
        } else if (id < id2) {
            return -1;
        } else {
            return id > id2 ? 1 : 0;
        }
    }

    private static TLRPC.TL_message map(DeletedMessageFull deletedMessageFull, int i) {
        TLRPC.Reaction reaction;
        TLRPC.TL_message tLRPC$TL_message = new TLRPC.TL_message();
        AyuMessageUtils.map(deletedMessageFull.message, tLRPC$TL_message, i);

        List<DeletedMessageReaction> list = deletedMessageFull.reactions;
        if (list != null && !list.isEmpty()) {
            tLRPC$TL_message.reactions = new TLRPC.TL_messageReactions();
            int i2 = 0;

            for (DeletedMessageReaction deletedMessageReaction : deletedMessageFull.reactions) {
                TLRPC.TL_reactionCount tLRPC$TL_reactionCount = new TLRPC.TL_reactionCount();
                tLRPC$TL_reactionCount.count = deletedMessageReaction.count;
                tLRPC$TL_reactionCount.chosen = deletedMessageReaction.selfSelected;
                i2++;
                tLRPC$TL_reactionCount.chosen_order = i2;

                if (deletedMessageReaction.isCustom) {
                    var customEmoji = new TLRPC.TL_reactionCustomEmoji();
                    customEmoji.document_id = deletedMessageReaction.documentId;
                    reaction = customEmoji;
                } else {
                    var emoji = new TLRPC.TL_reactionEmoji();
                    emoji.emoticon = deletedMessageReaction.emoticon;
                    reaction = emoji;
                }

                tLRPC$TL_reactionCount.reaction = reaction;
                tLRPC$TL_message.reactions.results.add(tLRPC$TL_reactionCount);
            }
        }

        tLRPC$TL_message.ayuDeleted = true;
        AyuMessageUtils.mapMedia(deletedMessageFull.message, tLRPC$TL_message);
        return tLRPC$TL_message;
    }
}
