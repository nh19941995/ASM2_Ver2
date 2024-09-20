
// hiển thị tên file tạm khi chọn file
document.addEventListener('DOMContentLoaded', function () {
    const cvFileInput = document.getElementById("cvfile");
    if (cvFileInput) {
        cvFileInput.addEventListener("change", function (event) {
            const files = event.target.files;
            const tempCVFile = document.getElementById("tempCVFile");
            if (tempCVFile) {
                tempCVFile.innerText = files[0] ? files[0].name  : 'No file selected';
            }
        });
    }
});

