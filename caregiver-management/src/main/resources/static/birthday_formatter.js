const birthdayFormatter = (function() {
    function strDateToSixDigit(date) {
        return date.replace(/-/g, '').slice(2);
    }

    function sixDigitToStrDate(birthday, genderCodeStr) {
        if (!/^\d{6}$/.test(birthday) || !/^\d{1,}$/.test(genderCodeStr)) return null;

        const genderCode = parseInt(genderCodeStr.charAt(0), 10);
        let century;

        switch (genderCode) {
            case 1: case 2: case 5: case 6:
                century = '19';
                break;
            case 3: case 4: case 7: case 8:
                century = '20';
                break;
            case 9: case 0:
                century = '18';
                break;
            default:
                return null; // 잘못된 코드
        }

        const year = century + birthday.slice(0, 2);
        const month = birthday.slice(2, 4);
        const day = birthday.slice(4, 6);

        return `${year}-${month}-${day}`;
    }

    // 모듈로 export (IIFE 리턴)
    return {
        strDateToSixDigit,
        sixDigitToStrDate,
    };
})();