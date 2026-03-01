
window.addEventListener('DOMContentLoaded', () => {
    loadList();
});

const input = document.querySelector('#custom-input');
const addBtn = document.querySelector('#custom-btn');

addBtn.addEventListener('click', () => {
    const value = input.value.trim();

    if (!value) {
        alert("확장자를 입력해주세요.");
        return;
    }

    fetch('/custom', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ extension: value })
    })
        .then(response => {
            if (response.ok) {
                input.value = '';
                loadList();
            } else {
                response.json()
                    .then(errorMessage => {
                        alert(errorMessage.message);
                    })
            }
        }).catch(err => console.error("전송 에러:", err));
});

input.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') addBtn.click();
})

function loadList() {
    fetch('/custom')
        .then(response => response.json())
        .then(dataList => {
            const listUl = document.querySelector('#item-list');
            listUl.innerHTML = '';
            dataList.forEach(item => {
                const li = document.createElement('li');
                li.className = 'tag-item';

                li.innerHTML = `
                    <span>${item.extension}</span>
                    <button class="delete-btn" onclick="deleteItem(this, '${item.extension}')">X</button>
                `;

                listUl.appendChild(li);
            });

            document.querySelector('#current-count').innerText = document.querySelectorAll('#item-list li').length;
        })
        .catch(error => console.error('커스텀 로드 에러:', error));
}

function deleteItem(buttonElement, extensionName) {
    fetch(`/custom?extension=${extensionName}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.status === 204) {
                const li = buttonElement.parentElement;
                li.remove();

                document.querySelector('#current-count').innerText = document.querySelectorAll('#item-list li').length;
            }
        })
        .catch(error => console.error('삭제 실패:', error));
}