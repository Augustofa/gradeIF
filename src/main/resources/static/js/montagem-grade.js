function addDisciplina() {
    const disciplinaSelect = document.getElementById('disciplina');
    const selectedDisciplina = disciplinaSelect.value;
    if (selectedDisciplina) {
        console.log('Disciplina adicionada:', selectedDisciplina);
    } else {
        alert('Por favor, selecione uma disciplina.');
    }
}