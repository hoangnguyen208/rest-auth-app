$(document).ready(() => {
  const urlParams = new URLSearchParams(location.search);

  if (urlParams.has("token")) {
    verifyToken(urlParams.get("token"));
  }

  function verifyToken(tokenValue) {
    $.get("http://localhost:8080/rest-photo-app/users/email-verification", {
      token: tokenValue
    }).done(res => {
      if (res["operationResult"] === "SUCCESS") {
        $("#failed").attr("style", "display: none !important");
        $("#success").attr("style", "display: block !important; color: teal;");
      } else {
        $("#failed").attr(
          "style",
          "display: block !important; color: crimson;"
        );
        $("#success").attr("style", "display: none !important");
      }
    });
  }
});
