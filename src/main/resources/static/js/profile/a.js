
// hiển thị tên file tạm khi chọn file
document.getElementById("cvfile").addEventListener("change", function (event) {
    const files = event.target.files;
    // thay đổi tempCVFile
    document.getElementById("tempCVFile").innerText = files[0].name;
});

console.log("js_profile.jsx is loaded");