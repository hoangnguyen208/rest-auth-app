$(document).ready(() => {
  const urlParams = new URLSearchParams(location.search);
  const form = document.getElementById("password-reset");

  function isEmpty(str) {
    return !str || 0 === str.trim().length;
  }

  form.addEventListener("submit", e => {
    e.preventDefault();
    if (!urlParams.has("token") || isEmpty(urlParams.get("token"))) {
      alert("Missing the required token");
      return;
    }

    const tokenValue = urlParams.get("token");
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirmPassword").value;

    if (isEmpty(password)) {
      alert("Password cannot be empty");
      return;
    }

    if (isEmpty(confirmPassword)) {
      alert("Confirm Password cannot be empty");
      return;
    }

    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    $.ajaxSetup({
      contentType: "application/json"
    });

    let payload = { token: tokenValue, password: password };

    $.post(
      "http://localhost:8080/rest-photo-app/users/password-reset",
      JSON.stringify(payload)
    ).done(res => {
      document.getElementById("password").value = "";
      document.getElementById("confirmPassword").value = "";

      if (res["operationResult"] === "SUCCESS") {
        $("#failed").attr("style", "display: none !important;");
        $("#success").attr("style", "display: block !important; color: teal;");
        $("#form").attr("style", "display: none !important");
      } else {
        $("#failed").attr(
          "style",
          "display: block !important; color: crimson;"
        );
        $("#success").attr("style", "display: none !important");
        $("#form").attr("style", "display: block !important");
      }
    });
  });
});
