import React from 'react';
import {Container, Title, TextInput, List, Checkbox, MantineProvider} from '@mantine/core';
import {Todo} from './types';

import '@mantine/core/styles.css';

type AppProps = {
    todos: Todo[];
}

export const App: React.FC = (props: AppProps) => {
    const initialTodos = props.todos;
    const [todos, setTodos] = React.useState<Todo[]>(initialTodos);

    const handleAddTodo = (title: string) => {
        const newTodo = {id: Date.now(), title, completed: false};
        setTodos([...todos, newTodo]);
        fetch('/api', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newTodo),
        });
    };

    const handleToggleTodo = (id: number) => {
        let updated = todos.map(todo =>
            todo.id === id ? {...todo, completed: !todo.completed} : todo
        );
        const todo = updated.find(todo => todo.id === id);
        if (todo) {
            setTodos(updated);
            fetch(`/api/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(todo),
            });
        }
    };

    return (
        <MantineProvider deduplicateCssVariables={false}>
            <Container>
                <Title>TODO App</Title>
                <TextInput
                    placeholder="Add a new todo"
                    onKeyDown={(e) => {
                        if (e.key === 'Enter' && e.currentTarget.value) {
                            handleAddTodo(e.currentTarget.value);
                            e.currentTarget.value = '';
                        }
                    }}
                />
                <List>
                    {todos.map(todo => (
                        <List.Item key={todo.id}>
                            <Checkbox
                                id={`todo_${todo.id}`}
                                checked={todo.completed}
                                onChange={() => handleToggleTodo(todo.id)}
                                label={todo.title}
                            />
                        </List.Item>
                    ))}
                </List>
            </Container>
        </MantineProvider>
    );
};