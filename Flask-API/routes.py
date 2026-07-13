from flask import Blueprint, request, jsonify
from models import db, Cita

citas_bp = Blueprint('citas_bp', __name__)

# Obtener todas las citas
@citas_bp.route('/citas', methods=['GET'])
def obtener_citas():
    citas = Cita.query.all()
    resultado = [cita.to_dict() for cita in citas]
    return jsonify(resultado), 200

# Obtener una cita por ID
@citas_bp.route('/citas/<int:id>', methods=['GET'])
def obtener_cita(id):
    cita = Cita.query.get(id)
    if cita is None:
        return jsonify({"error": "Cita no encontrada"}), 404
    return jsonify(cita.to_dict()), 200

# Crear una nueva cita
@citas_bp.route('/citas', methods=['POST'])
def crear_cita():
    datos = request.get_json()

    if not datos:
        return jsonify({"error": "No se recibieron datos"}), 400

    campos_requeridos = ['paciente', 'doctor', 'especialidad', 'fecha', 'hora']
    for campo in campos_requeridos:
        if campo not in datos or not str(datos[campo]).strip():
            return jsonify({"error": f"El campo '{campo}' es obligatorio"}), 400

    nueva_cita = Cita(
        paciente=datos['paciente'],
        doctor=datos['doctor'],
        especialidad=datos['especialidad'],
        fecha=datos['fecha'],
        hora=datos['hora'],
        motivo=datos.get('motivo', ''),
        estado=datos.get('estado', 'Pendiente')
    )

    db.session.add(nueva_cita)
    db.session.commit()

    return jsonify(nueva_cita.to_dict()), 201

# Actualizar una cita existente
@citas_bp.route('/citas/<int:id>', methods=['PUT'])
def actualizar_cita(id):
    cita = Cita.query.get(id)
    if cita is None:
        return jsonify({"error": "Cita no encontrada"}), 404

    datos = request.get_json()
    if not datos:
        return jsonify({"error": "No se recibieron datos"}), 400

    cita.paciente = datos.get('paciente', cita.paciente)
    cita.doctor = datos.get('doctor', cita.doctor)
    cita.especialidad = datos.get('especialidad', cita.especialidad)
    cita.fecha = datos.get('fecha', cita.fecha)
    cita.hora = datos.get('hora', cita.hora)
    cita.motivo = datos.get('motivo', cita.motivo)
    cita.estado = datos.get('estado', cita.estado)

    db.session.commit()

    return jsonify(cita.to_dict()), 200

# Eliminar una cita
@citas_bp.route('/citas/<int:id>', methods=['DELETE'])
def eliminar_cita(id):
    cita = Cita.query.get(id)
    if cita is None:
        return jsonify({"error": "Cita no encontrada"}), 404

    db.session.delete(cita)
    db.session.commit()

    return jsonify({"mensaje": "Cita eliminada correctamente"}), 200