const userManager = (function() {
    // 내부 상태
    let currentUser = null;       // 메모리에 저장된 현재 유저 정보
    let isLoading = false;        // 서버에서 가져오는 중인지 표시
    let readCallbacks = [];       // 유저 정보가 로딩된 후에 실행되어야 할 함수(람다)들

    function loadUserFromStorage() {
        try {
            const stored = localStorage.getItem("currentUser");
            if (stored) {
                currentUser = JSON.parse(stored);
            }
        } catch (e) {
            console.error("Failed to parse currentUser from localStorage", e);
        }
    }

    function fetchUserFromServer() {
        isLoading = true;
        $.ajax({
            type: "GET",
            url: "/api/v1/users/" + authTokenManager.getUserId(),
            success: function(data) {
                // 서버에서 받아온 데이터를 메모리에 저장
                currentUser = data;
                // localStorage에도 캐싱
                localStorage.setItem("currentUser", JSON.stringify(data));

                // 지금까지 대기 중인 read() 콜백들 모두 실행
                readCallbacks.forEach(fn => {
                    fn(currentUser);
                });
                // 리스트 초기화
                readCallbacks = [];
            },
            error: function(error) {
                console.error("유저 정보를 가져오지 못했습니다.", error);
                // 실패 시 에러 처리 로직 추가
            },
            complete: function() {
                isLoading = false;
            }
        });
    }

    // 실제로 외부에서 호출하는 함수
    // (사용 예: userManager.ready(user => { console.log(user.name); }))
    function ready(callback) {
        if (!currentUser) {
            loadUserFromStorage();
        }

        // 메모리에 유저 정보가 이미 있으면 즉시 callback 실행
        if (currentUser) {
            callback(currentUser);
        } else {
            readCallbacks.push(callback);
            if (!isLoading) {
                fetchUserFromServer();
            }
        }
    }
    function getCurrentUser() {
        return currentUser;
    }

    // 모듈로 export (IIFE 리턴)
    return {
        ready,
        getCurrentUser,
    };
})();