from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

class Cita(db.Model):
    __tablename__ = 'citas'

    id = db.Column(db.Integer, primary_key=True)
    paciente = db.Column(db.String(100), nullable=False)
    doctor = db.Column(db.String(100), nullable=False)
    especialidad = db.Column(db.String(100), nullable=False)
    fecha = db.Column(db.String(20), nullable=False)  
    hora = db.Column(db.String(10), nullable=False)   
    motivo = db.Column(db.String(255), nullable=True)
    estado = db.Column(db.String(20), nullable=False, default='Pendiente')

    def to_dict(self):
        return {
            "id": self.id,
            "paciente": self.paciente,
            "doctor": self.doctor,
            "especialidad": self.especialidad,
            "fecha": self.fecha,
            "hora": self.hora,
            "motivo": self.motivo,
            "estado": self.estado
        }