function addDisciplina() {
    const disciplinaSelect = document.getElementById('disciplina');
    const selectedDisciplina = disciplinaSelect.value;
    if (selectedDisciplina) {
        // Add the selected disciplina to a list (you can customize this part as needed)
        console.log('Disciplina adicionada:', selectedDisciplina);
    } else {
        alert('Por favor, selecione uma disciplina.');
    }
}