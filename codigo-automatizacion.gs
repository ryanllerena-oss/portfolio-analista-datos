/**
 * Automatización de candidatos para Google Sheets.
 * Proyecto demostrativo de Arud Rayn Sánchez Llerena.
 * Columnas esperadas: ID, Fecha, Nombre, Email, Vacante, Fuente, Estado.
 */
function registrarCandidato(datos) {
  validarCandidato(datos);
  const hoja = SpreadsheetApp.getActive().getSheetByName('Candidatos');
  if (!hoja) throw new Error('No existe la hoja Candidatos.');
  const id = 'CAN-' + Utilities.getUuid().slice(0, 8).toUpperCase();
  const fecha = new Date();
  const estado = 'Recibido';
  hoja.appendRow([id, fecha, datos.nombre.trim(), datos.email.trim().toLowerCase(), datos.vacante, datos.fuente, estado]);
  enviarConfirmacion(datos.email, datos.nombre, datos.vacante, id);
  return { id: id, estado: estado, fecha: fecha };
}

function validarCandidato(datos) {
  const requeridos = ['nombre', 'email', 'vacante', 'fuente'];
  requeridos.forEach(function(campo) {
    if (!datos[campo] || String(datos[campo]).trim() === '') throw new Error('Campo obligatorio: ' + campo);
  });
  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!patronEmail.test(datos.email)) throw new Error('Correo electrónico inválido.');
}

function enviarConfirmacion(email, nombre, vacante, id) {
  const asunto = 'Postulación recibida · ' + vacante;
  const mensaje = 'Hola ' + nombre + ',\n\nRecibimos tu postulación para ' + vacante + '.\nCódigo de seguimiento: ' + id + '\n\nGracias.';
  MailApp.sendEmail(email, asunto, mensaje);
}

function procesarFormulario(e) {
  return registrarCandidato({
    nombre: e.namedValues['Nombre'][0],
    email: e.namedValues['Correo'][0],
    vacante: e.namedValues['Vacante'][0],
    fuente: e.namedValues['Fuente'][0]
  });
}
