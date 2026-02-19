let subjectCount = 0;
let generatedTimetable = [];
let isLoading = false;

/* ===============================
   ADD SUBJECT
================================ */
function addSubject() {
    subjectCount++;

    let div = document.createElement("div");
    div.classList.add("subject-item");

    div.innerHTML = `
        <input type="text" placeholder="Subject Name" id="name${subjectCount}">
        <input type="number" placeholder="Hours per Week" id="hours${subjectCount}">
        <input type="text" placeholder="Faculty Name" id="faculty${subjectCount}">
        <button onclick="this.parentElement.remove()" class="btn-remove">✖</button>
    `;

    document.getElementById("subjects").appendChild(div);
}

/* ===============================
   GENERATE TIMETABLE
================================ */
function generateTimetable() {

    if (isLoading) return;

    let subjects = [];
    let subjectNames = new Set();

    for (let i = 1; i <= subjectCount; i++) {

        let name = document.getElementById("name" + i)?.value.trim();
        let hours = document.getElementById("hours" + i)?.value;
        let faculty = document.getElementById("faculty" + i)?.value.trim();

        if (name && hours && faculty) {

            if (subjectNames.has(name.toLowerCase())) {
                showToast("Duplicate subject detected: " + name, true);
                return;
            }

            subjectNames.add(name.toLowerCase());

            subjects.push({
                name: name,
                hoursPerWeek: parseInt(hours),
                facultyName: faculty
            });
        }
    }

    if (subjects.length === 0) {
        showToast("Please add at least one subject!", true);
        return;
    }

    let requestData = {
        numberOfDays: parseInt(document.getElementById("days").value),
        periodsPerDay: parseInt(document.getElementById("periods").value),
        startTime: document.getElementById("startTime").value,
        periodDuration: parseInt(document.getElementById("duration").value),
        breakAfterPeriod: parseInt(document.getElementById("breakAfter").value),
        breakDuration: parseInt(document.getElementById("breakDuration").value),
        lunchAfterPeriod: parseInt(document.getElementById("lunchAfter").value),
        lunchDuration: parseInt(document.getElementById("lunchDuration").value),
        subjects: subjects
    };

    toggleLoading(true);

    fetch("http://localhost:8080/api/timetable/generate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        generatedTimetable = data;
        displayTimetable(data);
        showToast("Timetable Generated Successfully 🚀");
        document.getElementById("timetable").scrollIntoView({ behavior: "smooth" });
    })
    .catch(() => showToast("Server Error! Check backend.", true))
    .finally(() => toggleLoading(false));
}

/* ===============================
   DISPLAY TIMETABLE
================================ */
function displayTimetable(data) {

    let html = "<table class='fade-in'>";
    html += "<tr><th>Day</th><th>Period</th><th>Start</th><th>End</th><th>Type</th><th>Subject</th></tr>";

    data.forEach(entry => {

        let className = "";
        if (entry.type.toLowerCase() === "break") className = "break";
        else if (entry.type.toLowerCase() === "lunch") className = "lunch";

        html += `
            <tr class="${className}">
                <td>${entry.day}</td>
                <td>${entry.periodNumber}</td>
                <td>${entry.startTime}</td>
                <td>${entry.endTime}</td>
                <td>${entry.type}</td>
                <td>${entry.subject ? entry.subject.name : "-"}</td>
            </tr>
        `;
    });

    html += "</table>";
    document.getElementById("timetable").innerHTML = html;
}

/* ===============================
   DOWNLOAD PDF
================================ */
function downloadPDF() {

    if (generatedTimetable.length === 0) {
        showToast("Generate timetable first!", true);
        return;
    }

    fetch("http://localhost:8080/api/pdf/generate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(generatedTimetable)
    })
    .then(response => response.blob())
    .then(blob => {

        let today = new Date().toISOString().split("T")[0];
        let url = window.URL.createObjectURL(blob);

        let a = document.createElement("a");
        a.href = url;
        a.download = `timetable-${today}.pdf`;
        a.click();

        showToast("PDF Downloaded 📄");
    })
    .catch(() => showToast("PDF Generation Failed!", true));
}

/* ===============================
   LOADING TOGGLE
================================ */
function toggleLoading(state) {

    isLoading = state;

    let btn = document.getElementById("generateBtn");

    if (state) {
        btn.disabled = true;
        btn.innerHTML = "Generating...";
        document.getElementById("timetable").innerHTML =
            `<div class="loader"></div>`;
    } else {
        btn.disabled = false;
        btn.innerHTML = "Generate Timetable";
    }
}

/* ===============================
   TOAST NOTIFICATION
================================ */
function showToast(message, isError = false) {

    let toast = document.createElement("div");
    toast.className = "toast";
    toast.innerText = message;

    if (isError) toast.classList.add("error");

    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add("show"), 100);

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.remove(), 500);
    }, 3000);
}
