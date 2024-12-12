$(document).ready(function () {

    // Configuración de la acción de logout con confirmación
    const btnLogout = $(".btnLogout");
    if (btnLogout.length) {
        btnLogout.on("click", function (e) {
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
                    // Redirige al enlace de cierre de sesión
                    window.location.href = $(this).attr('pag-redirect');
                }
            });
        });
    }

    // Inicialización del slider (Carrusel)
    const slider = $(".slider");
    if (slider.length) {
        slider.slick({
            centerMode: true,
            slidesToShow: 3,
            slidesToScroll: 1,
            infinite: true,
            dots: true,
            arrows: false,
            autoplay: true,
            autoplaySpeed: 2000,
            accessibility: false,
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1,
                        centerMode: false,
                    }
                },
            ]
        });
    }

    // Acción de compra de producto desde el formulario
    const btnComprar = $(".btnComprar");
    const formProducto = document.getElementById('formularioProducto');
    if (btnComprar.length && formProducto) {
        btnComprar.on("click", function (e) {
            e.preventDefault();
            formProducto.action = $(this).data('url');
            formProducto.method = 'get';
            formProducto.submit();
        });
    }

    // Actualización del costo de envío cuando se selecciona producto o sucursal
    const productoInput = document.getElementById("producto");
    const sucursalSelect = document.getElementById("sucursal");
    const formPedido = document.getElementById("formularioPedido");
    if (productoInput && sucursalSelect && formPedido) {
        const productoId = productoInput.value;
        sucursalSelect.addEventListener("change", function () {
            const sucursalId = sucursalSelect.value;
            if (formPedido.contains(sucursalSelect) && (productoId || sucursalId)) {
                // Realiza solicitud para calcular el envío
                fetch(`/pedido/calcularEnvio?sucursalId=${sucursalId}&productoId=${productoId}`)
                    .then(response => response.json())
                    .then(data => {
                        // Actualiza los campos con la información de envío
                        document.getElementById("costoEnvio").value = data.costoEnvio;
                        document.getElementById("diasEspera").value = data.diasEspera;
                    })
                    .catch(error => console.error("Error al obtener el costo de envío:", error));
            } else {
                alert("Por favor, seleccione un producto y una sucursal.");
            }
        });
    }

    // Validación del formulario de login
    const formLogin = $("#formLogin");
    if (formLogin.length) {
        formLogin.on("submit", function (e) {
            e.preventDefault();

            let esValido = true;

            // Validación del correo
            const correo = $('#username').val();
            const correoError = $('#correoError');
            const correoPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!correoPattern.test(correo)) {
                correoError.text('Por favor, ingrese un correo válido.');
                esValido = false;
            } else {
                correoError.text('');
            }

            // Validación de la contraseña
            const password = $('#password').val();
            const passwordError = $('#passwordError');
            if (password.length < 6) {
                passwordError.text('La contraseña debe tener al menos 6 caracteres.');
                esValido = false;
            } else {
                passwordError.text('');
            }

            // Si todo es válido, se envía el formulario
            if (esValido) {
                this.submit();
            }
        });
    }

    // Validación del formulario de registro
    const formRegistro = $("#formRegistro");
    if (formRegistro.length) {
        formRegistro.on("submit", function (e) {
            e.preventDefault();

            let esValido = true;

            // Validación de nombre
            const nombre = document.getElementById('nombre').value;
            const nombreError = document.getElementById('nombreError');
            if (nombre.trim() === '') {
                nombreError.textContent = 'Por favor, ingrese su nombre.';
                esValido = false;
            } else {
                nombreError.textContent = '';
            }

            // Validación de apellido
            const apellido = document.getElementById('apellido').value;
            const apellidoError = document.getElementById('apellidoError');
            if (apellido.trim() === '') {
                apellidoError.textContent = 'Por favor, ingrese su apellido.';
                esValido = false;
            } else {
                apellidoError.textContent = '';
            }

            // Validación de correo
            const correo = document.getElementById('correo').value;
            const correoError = document.getElementById('correoError');
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!emailPattern.test(correo)) {
                correoError.textContent = 'Por favor, ingrese un correo válido.';
                esValido = false;
            } else {
                correoError.textContent = '';
            }

            // Validación del teléfono
            const telefono = document.getElementById('telefono').value;
            const telefonoError = document.getElementById('telefonoError');
            const phonePattern = /^\+?\d{0,3}[-.\s]?\d?[-.\s]?\(?\d{2,4}\)?[-.\s]?\d{2,4}[-\s]?\d{4}$/;
            if (telefono && !phonePattern.test(telefono)) {
                telefonoError.textContent = 'Por favor, ingrese un teléfono válido.';
                esValido = false;
            } else {
                telefonoError.textContent = '';
            }

            // Validación de la contraseña
            const password = document.getElementById('password').value;
            const passwordError = document.getElementById('passwordError');
            if (password.length < 6) {
                passwordError.textContent = 'La contraseña debe tener al menos 6 caracteres.';
                esValido = false;
            } else {
                passwordError.textContent = '';
            }

            // Validación de la contraseña repetida
            const repeatPassword = document.getElementById('repetirPassword').value;
            const repeatPasswordError = document.getElementById('repetirPasswordError');
            if (repeatPassword !== password) {
                repeatPasswordError.textContent = 'Las contraseñas no coinciden.';
                esValido = false;
            } else {
                repeatPasswordError.textContent = '';
            }

            // Validación de aceptación de términos y condiciones
            const terminos = document.getElementById('terminos').checked;
            const terminosError = document.getElementById('terminosError');
            if (!terminos) {
                terminosError.textContent = 'Debe aceptar los términos y condiciones.';
                esValido = false;
            } else {
                terminosError.textContent = '';
            }

            if (esValido) {
                this.submit();
            }
        });
    }

    // Validación y vista previa de imagen seleccionada
    const selectorImagen = $("#imagenes");
    if (selectorImagen.length) {
        selectorImagen.on("change", function (e) {
            e.preventDefault();
            const reader = new FileReader();
            reader.onload = function () {
                const output = document.getElementById("preview");
                output.src = reader.result;
                output.style.display = "block";
            };
            reader.readAsDataURL(event.target.files[0]);
        });
    }

    // Contador de caracteres en la descripción del producto
    const txtAreaDescripcion = $("#descripcion");
    if (txtAreaDescripcion.length) {
        txtAreaDescripcion.on("input", function (e) {
            e.preventDefault();
            const charCount = document.getElementById("charCountDescripcion");
            let currentLength = txtAreaDescripcion.val().length;
            const maxChars = 500;

            // Si excede el límite, cortar el texto
            if (currentLength > maxChars) {
                txtAreaDescripcion.val(txtAreaDescripcion.val().substring(0, maxChars));
                currentLength = maxChars;
            }

            // Actualizar el contador de caracteres
            charCount.textContent = `${currentLength}/${maxChars}`;
        });
    }
});
