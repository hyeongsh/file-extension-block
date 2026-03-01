window.addEventListener('DOMContentLoaded', () => {
    fetch('/fixed')
        .then(response => response.json())
        .then(dataList => {
            dataList.forEach(item => {
                const checkbox = document.querySelector(`input[value="${item.extension}"]`);

                if (checkbox) {
                    checkbox.checked = item.blocked;
                }
            });
        })
        .catch(error => console.error('fixed 로드 실패:', error));
})

const checkboxes = document.querySelectorAll('input[type="checkbox"]');

checkboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', (event) => {
        const data = {
            extension: event.target.value,
            block: event.target.checked
        };

        fetch('/fixed', {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(result => console.log('성공:', result))
            .catch(error => console.error('에러:', error));
    });
});