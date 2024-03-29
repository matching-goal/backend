package matchingGoal.matchingGoal.chat.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.dto.ChatHistoryDto;
import matchingGoal.matchingGoal.chat.dto.ChatRoomMemberDto;
import matchingGoal.matchingGoal.chat.dto.CreateChatRoomRequestDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponseDto;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public String createChatRoom(String token, CreateChatRoomRequestDto request) {

    jwtTokenProvider.validateToken(token);

    long hostId = jwtTokenProvider.getId(token);
    Member host = getMember(hostId);
    Member guest = getMember(request.getGuestId());
    long matchingBoardId = request.getMatchingBoardId();
    Set<Member> members = new HashSet<>();
    members.add(host);
    members.add(guest);
    Optional<ChatRoom> optionalChatRoom = chatRoomRepository
        .findByMatchingBoardIdAndChatRoomMembers(matchingBoardId, members);

    if (optionalChatRoom.isPresent()) {

      return optionalChatRoom.get().getId();
    } else {
      ChatRoom room = ChatRoom.create(request, members);

      chatRoomRepository.save(room);

      log.info(room.getId());

      return room.getId();
    }
  }

  public void addMembers(String chatRoomId, Set<Member> members) {

    ChatRoom room = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

    room.addMembers(members);
    log.info(chatRoomId + " 에 " + members.toString() + " 추가");
  }

  public List<ChatRoomListResponseDto> myChat(String token) {

    jwtTokenProvider.validateToken(token);
    long memberId = jwtTokenProvider.getId(token);
    List<ChatRoom> myChat = chatRoomRepository.findListsByChatRoomMembersId(memberId);

    return myChat.stream().map(ChatRoomListResponseDto::fromEntity).toList();
  }

  @Transactional
  public void quit(String token, String chatRoomId) {

    jwtTokenProvider.validateToken(token);
    long memberId = jwtTokenProvider.getId(token);
    ChatRoom chatRoom = getChatRoom(chatRoomId);

    chatRoom.quit(memberId);
  }

  @Transactional
  public List<ChatMessageDto> getChatMessage(String chatRoomId) {

    List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);

    for (ChatMessage chat : chatMessageList) {
      chat.changeReadYn(1);
    }

    return chatMessageList.stream()
        .map(ChatMessageDto::fromEntity).toList();
  }

  public ChatHistoryDto getChatHistory(String token, String chatRoomId) {

    jwtTokenProvider.validateToken(token);
    long memberId = jwtTokenProvider.getId(token);
    boolean isChatRoomMember = false;
    ChatRoom chatRoom = getChatRoom(chatRoomId);
    List<ChatRoomMemberDto> chatRoomMemberInfo = chatRoom.getChatRoomMembers().stream()
        .map(ChatRoomMemberDto::fromEntity).toList();

    for (ChatRoomMemberDto member : chatRoomMemberInfo) {
      if (member.getMemberId() == memberId) {
        isChatRoomMember = true;
        break;
      }
    }
    if (!isChatRoomMember) {
      throw new CustomException(ErrorCode.NOT_CHATROOM_MEMBER);
    }

    List<ChatMessageDto> chatMessageList = getChatMessage(chatRoomId);

    return ChatHistoryDto.builder()
        .chatRoomMemberInfo(chatRoomMemberInfo)
        .chatMessageList(chatMessageList)
        .build();
  }

  public List<ChatRoom> chatRoomList(long matchingBoardId) {

    return chatRoomRepository.findAllByMatchingBoardId(matchingBoardId);
  }

  public ChatRoom getChatRoom(String chatRoomId) {

    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
  }

  public Member getMember(long userId) {

    return memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }

  public void closeAllChatRoom(long matchingBoardId) {

    List<ChatRoom> chatRoomList = chatRoomList(matchingBoardId);

    for (ChatRoom chatRoom : chatRoomList) {
      closeChatRoom(chatRoom.getId());
    }
  }

  @Transactional
  public void closeChatRoom(String ChatRoomId) {

    ChatRoom chatRoom = chatRoomRepository.findById(ChatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

    chatRoom.close();
  }

}
