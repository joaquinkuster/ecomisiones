$(document).ready(function () {
    const btnLogout = $(".btnLogout");
        $(btnLogout).on("click", function (e) {
        e.preventDefault();
        Swal.fire({
            title: "¿Estás seguro?",
            text: "Vas a cerrar tu sesión actual.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Sí, cerrar sesión",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = $(this).attr('pag-redirect');
            }
        });
    });
})