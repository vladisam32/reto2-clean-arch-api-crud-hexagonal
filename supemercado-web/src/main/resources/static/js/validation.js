/**
 * Form validation for the Supermarket Management System
 */

$(document).ready(function() {
    // Product form validation
    if ($("#nombre").length) {
        initProductFormValidation();
    }

    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });
});

/**
 * Initialize product form validation
 */
function initProductFormValidation() {
    const form = document.querySelector('form');
    const nombreInput = document.getElementById('nombre');
    const descripcionInput = document.getElementById('descripcion');
    const precioInput = document.getElementById('precio');
    const categoriaInput = document.getElementById('categoria');
    const codigoBarrasInput = document.getElementById('codigoBarras');

    // Add event listeners for real-time validation
    nombreInput.addEventListener('input', function() {
        validateNombre(nombreInput);
    });

    descripcionInput.addEventListener('input', function() {
        validateDescripcion(descripcionInput);
    });

    precioInput.addEventListener('input', function() {
        validatePrecio(precioInput);
    });

    categoriaInput.addEventListener('input', function() {
        validateCategoria(categoriaInput);
    });

    codigoBarrasInput.addEventListener('input', function() {
        validateCodigoBarras(codigoBarrasInput);
    });

    // Form submission validation
    form.addEventListener('submit', function(event) {
        let isValid = true;
        
        // Validate all fields
        if (!validateNombre(nombreInput)) isValid = false;
        if (!validateDescripcion(descripcionInput)) isValid = false;
        if (!validatePrecio(precioInput)) isValid = false;
        if (!validateCategoria(categoriaInput)) isValid = false;
        if (!validateCodigoBarras(codigoBarrasInput)) isValid = false;
        
        if (!isValid) {
            event.preventDefault();
            showValidationMessage('Por favor, corrija los errores en el formulario antes de enviar.');
        }
    });
}

/**
 * Validate product name
 */
function validateNombre(input) {
    removeValidationClasses(input);
    
    if (input.value.trim() === '') {
        setInvalid(input, 'El nombre del producto es obligatorio');
        return false;
    } else if (input.value.trim().length < 3) {
        setInvalid(input, 'El nombre debe tener al menos 3 caracteres');
        return false;
    } else if (input.value.trim().length > 100) {
        setInvalid(input, 'El nombre no puede exceder los 100 caracteres');
        return false;
    } else {
        setValid(input);
        return true;
    }
}

/**
 * Validate product description
 */
function validateDescripcion(input) {
    removeValidationClasses(input);
    
    if (input.value.trim().length > 500) {
        setInvalid(input, 'La descripción no puede exceder los 500 caracteres');
        return false;
    } else {
        setValid(input);
        return true;
    }
}

/**
 * Validate product price
 */
function validatePrecio(input) {
    removeValidationClasses(input);
    
    const precio = parseFloat(input.value);
    
    if (input.value.trim() === '') {
        setInvalid(input, 'El precio es obligatorio');
        return false;
    } else if (isNaN(precio)) {
        setInvalid(input, 'El precio debe ser un número válido');
        return false;
    } else if (precio < 0) {
        setInvalid(input, 'El precio no puede ser negativo');
        return false;
    } else if (precio > 1000000) {
        setInvalid(input, 'El precio es demasiado alto');
        return false;
    } else {
        setValid(input);
        return true;
    }
}

/**
 * Validate product category
 */
function validateCategoria(input) {
    removeValidationClasses(input);
    
    if (input.value.trim() === '') {
        setInvalid(input, 'La categoría es obligatoria');
        return false;
    } else if (input.value.trim().length < 2) {
        setInvalid(input, 'La categoría debe tener al menos 2 caracteres');
        return false;
    } else if (input.value.trim().length > 50) {
        setInvalid(input, 'La categoría no puede exceder los 50 caracteres');
        return false;
    } else {
        setValid(input);
        return true;
    }
}

/**
 * Validate product barcode
 */
function validateCodigoBarras(input) {
    removeValidationClasses(input);
    
    const value = input.value.trim();
    const barcodeRegex = /^[0-9]{8,13}$/;
    
    if (value === '') {
        setInvalid(input, 'El código de barras es obligatorio');
        return false;
    } else if (!barcodeRegex.test(value)) {
        setInvalid(input, 'El código de barras debe tener entre 8 y 13 dígitos numéricos');
        return false;
    } else {
        setValid(input);
        return true;
    }
}

/**
 * Set input as valid
 */
function setValid(input) {
    input.classList.add('is-valid');
    
    // Remove any existing feedback
    const nextSibling = input.nextElementSibling;
    if (nextSibling && nextSibling.classList.contains('invalid-feedback')) {
        nextSibling.remove();
    }
}

/**
 * Set input as invalid with error message
 */
function setInvalid(input, message) {
    input.classList.add('is-invalid');
    
    // Remove any existing feedback
    const nextSibling = input.nextElementSibling;
    if (nextSibling && nextSibling.classList.contains('invalid-feedback')) {
        nextSibling.remove();
    }
    
    // Add error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    input.parentNode.insertBefore(errorDiv, input.nextSibling);
}

/**
 * Remove validation classes from input
 */
function removeValidationClasses(input) {
    input.classList.remove('is-valid', 'is-invalid');
    
    // Remove any existing feedback
    const nextSibling = input.nextElementSibling;
    if (nextSibling && nextSibling.classList.contains('invalid-feedback')) {
        nextSibling.remove();
    }
}

/**
 * Show validation message at the top of the form
 */
function showValidationMessage(message) {
    // Remove any existing alert
    const existingAlert = document.querySelector('.validation-alert');
    if (existingAlert) {
        existingAlert.remove();
    }
    
    // Create alert
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger validation-alert';
    alertDiv.textContent = message;
    
    // Insert at the top of the form
    const form = document.querySelector('form');
    form.insertBefore(alertDiv, form.firstChild);
    
    // Scroll to the top of the form
    alertDiv.scrollIntoView({ behavior: 'smooth' });
}