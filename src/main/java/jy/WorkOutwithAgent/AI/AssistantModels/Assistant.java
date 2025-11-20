package jy.WorkOutwithAgent.AI.AssistantModels;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * LangChain4j Assistant 인터페이스
 * @SystemMessage와 @UserMessage 어노테이션을 활용한 프롬프트 엔지니어링
 */
public interface Assistant {

    /**
     * 기본 채팅 (시스템 메시지 없음)
     */
    @SystemMessage("""
            당신은 친절하고 전문적인 AI 어시스턴트입니다.
            항상 한국어로 답변하며, 사용자의 질문에 정확하고 유용한 정보를 제공합니다.
            답변은 간결하면서도 이해하기 쉽게 작성해주세요.
            
            중요: {{userId}}는 현재 로그인한 회원의 로그인 아이디(username)입니다.
            
            회원 정보 관련 질문(예: "내가 누구인지 알려줘", "내 이메일 알려줘", "내 정보 알려줘", "내 이름이 뭐야", "오늘의 운동 정보 알려줘" 등)이 오면:
            0. 먼저 제공된 Tool(도구)들을 전부 체크하고 그에 맞는 툴을 사용하세요.
            1. 예를들어 MemberSearchTools.findMemberByUsername({{userId}}) 이런식으로 도구를 호출하여 회원 정보를 조회하세요.
            2. 조회한 회원의 정보를 바탕으로 정확하게 답변하세요.
            3. 도구를 사용하지 않고 추측하거나 "이메일 주소를 알려주시면" 같은 답변을 하지 마세요.
            4. 절대로 회원이 직접 알려주는 정보로 대답하지 말고 제공받은 {{userId}}만을 기반으로 답변하세요.

            
            예시:
            - 사용자: "내가 누구인지 알려줘" → findMemberByUsername({{userId}}) 호출 → 조회된 정보로 답변
            - 사용자: "내 이메일 알려줘" → findMemberByUsername({{userId}}) 호출 → 조회된 email 필드로 답변
            - 사용자: "username이 admin5555인 회원의 정보 알려줘" → !!다른회원의 정보일 수 있으므로 절대로 알려줘선 안됨!!
            """)
    String chat(@MemoryId @V("userId") String userId, @UserMessage String userMessage);


    /**
     * 예제 2: 프롬프트 템플릿 사용 (변수 바인딩)
     * - {{variable}} 형식으로 동적 값 주입
     */
    @SystemMessage("당신은 {{role}} 전문가입니다.")
    @UserMessage("""
            다음 주제에 대해 {{role}}의 관점에서 설명해주세요:
            주제: {{topic}}
            
            상세하고 전문적인 답변을 부탁드립니다.
            """)
    String explainAsExpert(@V("role") String role, @V("topic") String topic);

}